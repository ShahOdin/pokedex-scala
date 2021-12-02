package client

import cats.effect.kernel.Concurrent
import cats.effect.std.Console
import domain.Pokemon
import org.http4s.client.Client
import org.http4s.{Method, Request, Status, Uri}
import cats.syntax.all._

trait PokeApiClient[F[_]] {
  def lookupPokemonId(name: String): F[Option[PokemonId]]
  def lookupPokemon(id: PokemonId): F[Pokemon]
}

object PokeApiClient {
  def apply[F[_] : Concurrent: Console](client: Client[F]): PokeApiClient[F] = new PokeApiClient[F] {
    override def lookupPokemonId(name: String): F[Option[PokemonId]] = {
      val uri = Uri.unsafeFromString(s"https://pokeapi.co/api/v2/pokemon/${name}")
      val request = Request[F](
        method = Method.GET,
        uri = uri
      )
      client
        .run(request)
        .use[Option[PokemonId]] { response =>
          response.status match {
            case Status.Ok => response
              .as[PokemonId]
              .map(_.some)
              .onError(e => Console[F].println(s"failed to decode pokemonId: $e"))
            case Status.NotFound =>
              none[PokemonId].pure[F]
            case status =>
              Concurrent[F].raiseError(new Throwable(s"The api returned unexpected response with Status: $status"))
          }
        }
    }

    override def lookupPokemon(id: PokemonId): F[Pokemon] = {
      val uri = Uri.unsafeFromString(s"https://pokeapi.co/api/v2/pokemon-species/${id.value}")
      val request = Request[F](
        method = Method.GET,
        uri = uri
      )
      client
        .run(request)
        .use[Pokemon] { response =>
          response.status match {
            case Status.Ok => response
              .as[Pokemon]
              .onError(e => Console[F].println(s"failed to decode pokemon: $e"))
            case status =>
              Concurrent[F].raiseError(new Throwable(s"The api returned unexpected response with Status: $status"))
          }
        }
    }
  }
}