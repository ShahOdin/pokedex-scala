package domain

import enumeratum.EnumEntry
import enumeratum._
import io.circe.{Decoder, Encoder}

//todo: write tests to test the decoder
sealed trait Habitat extends EnumEntry with EnumEntry.Hyphencase
object Habitat extends Enum[Habitat]{
  case object Cave extends Habitat
  case object Forest extends Habitat
  case object Grassland extends Habitat
  case object Mountain extends Habitat
  case object Rare extends Habitat
  case object RoughTerrain extends Habitat
  case object Sea extends Habitat
  case object Urban extends Habitat
  case object WatersEdge extends Habitat

  implicit val decoder: Decoder[Habitat] = enumeratum.Circe.decoder(this)
  implicit val encoder: Encoder[Habitat] = enumeratum.Circe.encoder(this)
  override val values = findValues
}
