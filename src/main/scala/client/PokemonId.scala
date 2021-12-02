package client

import cats.effect.kernel.Concurrent
import io.circe.Decoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

private[client] case class PokemonId (value: Int) extends AnyVal

object PokemonId{
  implicit val decoder: Decoder[PokemonId] = Decoder.instance(cursor =>
    for {
      id <- cursor.get[Int]("id")
    } yield PokemonId(id)
  )

  implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, PokemonId] = jsonOf
}
