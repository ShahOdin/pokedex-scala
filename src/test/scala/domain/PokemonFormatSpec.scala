package domain

import io.circe.parser._
import io.circe.syntax._
import util.Tests

class PokemonFormatSpec extends munit.FunSuite {

  test("Pokemon encoder should encode a sample pokemon as expected.".tag(Tests.Unit)){
    val pokemon = Pokemon(
      name = "mewtwo",
      description = "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.",
      habitat = Habitat.Rare,
      isLegendary = true,
    )

    val Right(expectedJson) = parse(
      """{
        "name" : "mewtwo",
        "description": "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.",
        "habitat": "rare",
        "isLegendary": true
        }""".stripMargin
    )

    assertEquals(pokemon.asJson, expectedJson)
  }

}
