package logic

import cats.Monad
import cats.data.OptionT
import client.{TranslatedText, TranslatorClient}
import domain.{Habitat, Pokemon}
import cats.syntax.all._

//todo add tests for this.
trait GetTranslatedPokemon[F[_]] {
  def fetch(pokemonName: String): F[Option[Pokemon]]
}

object GetTranslatedPokemon{
  def apply[F[_]: Monad](translatorClient: TranslatorClient[F], getPokemon: GetPokemon[F]): GetTranslatedPokemon[F] = {
    pokemonName =>
      val optionT = for {
        pokemon <- OptionT(getPokemon.fetch(pokemonName))
        originalDescription = pokemon.description
        translatedDescription <- OptionT.whenF(pokemon.habitat == Habitat.Cave || pokemon.isLegendary)(
          translatorClient.translateToYoda(originalDescription)
        )
          .orElse(OptionT.liftF(translatorClient.translateToShakespeare(originalDescription)))
          .handleError(_ => TranslatedText(originalDescription))
      } yield pokemon.copy(description = translatedDescription.value)

      optionT.value
  }
}
