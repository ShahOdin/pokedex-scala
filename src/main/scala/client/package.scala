import cats.effect.kernel.Async
import cats.effect.Resource
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client

package object client {
  def createClient[F[_]: Async]: Resource[F, Client[F]] = BlazeClientBuilder[F].resource
}
