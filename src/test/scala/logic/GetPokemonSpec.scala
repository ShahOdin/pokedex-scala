package logic

import cats.effect.IO
import cats.effect.kernel.Resource
import client.{PokeApiClient, createClient}
import util.Tests

class GetPokemonSpec extends munit.CatsEffectSuite {

  //technically this is a unit test and should use a mocked PokeApiClient, but easier to use the real thing than mock data.
  val pokemonFinder: Resource[IO, GetPokemon[IO]] = createClient[IO]
    .map(PokeApiClient.apply[IO])
    .map(GetPokemon.apply[IO])

  test("PokemonFinder should successfully look up a valid pokemon name".tag(Tests.Integration)){
    pokemonFinder.use(
      _.fetch("mewtwo")
        .map(maybe => assert(maybe.isDefined))
    )
  }

  test("PokemonFinder should gracefully fail to look up an invalid pokemon name".tag(Tests.Integration)){
    pokemonFinder.use(
      _.fetch("chewbacca")
        .map(maybe => assert(maybe.isEmpty))
    )
  }
}
