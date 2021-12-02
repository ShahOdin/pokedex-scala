package client

import cats.effect.IO
import cats.effect.kernel.Resource
import cats.syntax.all._
import util.Tests

class PokeApiClientSpec extends munit.CatsEffectSuite {

  val pokeApiClient: Resource[IO, PokeApiClient[IO]] = createClient[IO].map(PokeApiClient.apply[IO])

  //these tests should technically be pact based. but it's easier to test them directly.

  test("PokeApiClient should successfully look up the pokemon id of a valid pokemon".tag(Tests.Integration)){
    pokeApiClient.use(
      _.lookupPokemonId("mewtwo")
        .map(assertEquals(_, PokemonId(150).some))
    )
  }

  test("PokeApiClient should gracefully fail to look up the pokemon id of an invalid pokemon".tag(Tests.Integration)){
    pokeApiClient.use(
      _.lookupPokemonId("chewbacca")
        .map(assertEquals(_, none))
    )
  }

  test("PokeApiClient should successfully look up the the metadata of a valid pokemonId".tag(Tests.Integration)){
    pokeApiClient.use(
      _.lookupPokemon(PokemonId(150))
        .attempt
        .map(_.isRight)
        .map(assert(_))
    )
  }

  test("PokeApiClient should gracefully fail to look up the the metadata of an invalid pokemonId".tag(Tests.Integration)){
    pokeApiClient.use(
      _.lookupPokemon(PokemonId(666))
        .attempt
        .map(_.isLeft)
        .map(assert(_))
    )
  }

}
