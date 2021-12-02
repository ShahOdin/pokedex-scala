package logic

import cats.Monad
import domain.Pokemon
import client.PokeApiClient
import cats.syntax.all._

trait GetPokemon[F[_]] {
  def fetch(name: String): F[Option[Pokemon]]
}

object GetPokemon{
  def apply[F[_]: Monad](pokeApiClient: PokeApiClient[F]): GetPokemon[F] = name =>
    pokeApiClient.lookupPokemonId(name).flatMap(
      _.traverse(pokeApiClient.lookupPokemon)
    )
}
