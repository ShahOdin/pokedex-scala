package domain

import cats.effect.kernel.Concurrent
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe._

case class Pokemon(name: String, description: String, habitat: Habitat, isLegendary: Boolean)

object Pokemon{
  implicit val encoder: Encoder[Pokemon] = deriveEncoder
  implicit val decoder: Decoder[Pokemon] = Decoder.instance{ cursor =>
    for {
      name <- cursor.get[String]("name")
      habitat <- cursor.downField("habitat").downField("name").as[Habitat]
      isLegendary <- cursor.get[Boolean]("is_legendary")
      description <- cursor.downField("flavor_text_entries").downArray.downField("flavor_text").as[String].map(_.replaceAll("\\p{C}", " "))
    } yield Pokemon(name, description, habitat, isLegendary)

  }
  implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, Pokemon] = jsonOf
  implicit def entityEncoder[F[_]]: EntityEncoder[F, Pokemon] = jsonEncoderOf
}
