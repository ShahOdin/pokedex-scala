package util

import munit.Tag

sealed abstract class Tests(name: String) extends Tag(name)

object Tests {
  object Unit extends Tests("unit")
  object Integration extends Tests("unit")
}
