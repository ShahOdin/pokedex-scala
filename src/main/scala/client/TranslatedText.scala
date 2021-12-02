package client

import cats.effect.kernel.Concurrent
import io.circe.Decoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

private[client] case class TranslatedText(value: String) extends AnyVal

object TranslatedText {

  implicit val decoder: Decoder[TranslatedText] = Decoder.instance(
    _.downField("contents").downField("translated").as[String].map(TranslatedText.apply)
  )

  implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, TranslatedText] = jsonOf

}
