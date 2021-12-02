package client

import cats.effect.kernel.Concurrent
import cats.effect.std.Console
import org.http4s.{Method, Request, Status, Uri}
import org.http4s.client.Client
import org.http4s.QueryParamKeyLike.stringKey
import cats.syntax.all._

//todo add tests for this.
trait TranslatorClient[F[_]] {
  def translateToYoda(input: String): F[TranslatedText]
  def translateToShakespeare(input: String): F[TranslatedText]
}

object TranslatorClient{

  def apply[F[_] : Concurrent: Console](client: Client[F]): TranslatorClient[F] = new TranslatorClient[F] {
    override def translateToYoda(input: String): F[TranslatedText] = {
      val uri = Uri.unsafeFromString(s"https://api.funtranslations.com/translate/yoda.json").withQueryParam(
        "text",
        input
      )

      val request = Request[F](
        method = Method.GET,
        uri = uri
      )
      client
        .run(request)
        .use[TranslatedText] { response =>
          response.status match {
            case Status.Ok => response
              .as[TranslatedText]
              .onError(e => Console[F].println(s"failed to decode translated text: $e"))
            case status =>
              Concurrent[F].raiseError(new Throwable(s"The api returned unexpected response with Status: $status"))
          }
        }
    }

    override def translateToShakespeare(input: String): F[TranslatedText] = {
      val uri = Uri.unsafeFromString(s"https://api.funtranslations.com/translate/shakespeare.json").withQueryParam(
        "text",
        input
      )

      val request = Request[F](
        method = Method.GET,
        uri = uri
      )
      client
        .run(request)
        .use[TranslatedText] { response =>
          response.status match {
            case Status.Ok => response
              .as[TranslatedText]
              .onError(e => Console[F].println(s"failed to decode translated text: $e"))
            case status =>
              Concurrent[F].raiseError(new Throwable(s"The api returned unexpected response with Status: $status"))
          }
        }
    }
  }

}