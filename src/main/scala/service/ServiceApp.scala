package service

import cats.effect.std.Console
import cats.effect.{Async, IO, IOApp, Resource}
import client.{PokeApiClient, TranslatorClient, createClient}
import com.comcast.ip4s._
import logic.{GetPokemon, GetTranslatedPokemon}
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import service.routes.{GetPokemonRoute, GetTranslatedPokemonRoute}
import cats.implicits._

object ServiceApp extends IOApp.Simple {

  def serve[F[_]: Async](
                          service: HttpRoutes[F]
                        ): Resource[F, Server] = EmberServerBuilder
      .default[F]
      .withHttpApp(service.orNotFound)
      .withPort(port"5000")
      .build

  def create[F[_]: Async: Console]: Resource[F, Server] = for {
    pokeApiUnderlyingClient <- createClient
    translatorUnderlyingClient <- createClient
    pokeApiClient = PokeApiClient(pokeApiUnderlyingClient)
    translatorClient = TranslatorClient(translatorUnderlyingClient)
    getPokemon = GetPokemon(pokeApiClient)
    getTranslatedPokemon = GetTranslatedPokemon(translatorClient, getPokemon)
    httpApp = GetPokemonRoute(getPokemon) <+> GetTranslatedPokemonRoute(getTranslatedPokemon)
    server <- serve(httpApp)
  } yield server

  implicit val console: Console[IO] = Console.make

  override def run: IO[Unit] = create[IO].use { server =>
    console.println(s"Server Has Started at ${server.address}") >>
      Async[IO].never[Unit]
  }

}
