package scalaz6

sealed trait BooleanConjunction extends NewType[Boolean]

trait BooleanConjunctions {
  def conjunction(b: Boolean) = new BooleanConjunction {
    val value = b
  }
}
