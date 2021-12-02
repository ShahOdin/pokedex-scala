package service.routes

import cats.MonadThrow
import logic.GetPokemon
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.syntax.all._

object GetPokemonRoute {

  def apply[F[_]: MonadThrow](pokemonFinder: GetPokemon[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]{
      case GET -> Root / "pokemon" / name =>
        pokemonFinder
          .fetch(name)
          .flatMap {
            case Some(value) => Ok(value)
            case None => NotFound("no pokemon was found with that name")
          }
    }
  }

}
