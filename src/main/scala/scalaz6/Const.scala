package scalaz6

/**
 * Used to generate Phantom Applicative Functors and categories from a Monoidal type A
 *
 * @see scalaz6.Applicative#MonoidalApplicative
 * @see scalaz6.Category#MonoidCategory
 */
case class Const[A, +B](value: A) extends NewType[A]
case class Const2[A, +B, +C](value: A) extends NewType[A]
