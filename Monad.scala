object Monad extends App {
  val taco = new Taco[Int](5)
  val just = Just[Int](5)
  val nothing = Nothing[Int]()
  MonadTest.test2(taco)
  MonadTest.test2(just)
  MonadTest.test2(nothing)
}

object MonadTest {
  def fn(v: Int) = 2 * v
  def test[M <: Monad[Int]](m: M): Unit = {
    val res = m.bind(x => m.ret(fn(x)))
    println(res)
  }
  def test2[M <: Monad[Int]](m: M): Unit = {
    println(m.fmap(fn))
  }
}

class Taco[A](x: A) extends Monad[A] {
  type T[B] = Taco[B]
  def bind[B](b: A => T[B]) = b(x)
  def ret[B](input: B) = new Taco(input)
  def fmap[B](f: A => B): T[B] = ret(f(x))
  override def toString(): String = "Taco " + x.toString()
}

object MaybeTest {
  val start: Maybe[Int] = Just(5)
  val nothing: Maybe[Int] = Nothing()
  def test(v: Int): Maybe[Int] = Just(2 * v)
  def run(): Unit = {
    val test1 = start.bind(test).bind(test)
    val test2 = nothing.bind(test).bind(test)
    println(test1)
    println(test2)
  }
}

abstract class Monad[A] extends Functor[A] {
  type T[C] <: Monad[C]
  def bind[B](b: A => T[B]): T[B]
  def ret[B](input: B): T[B]
}

sealed trait Maybe[A] extends Monad[A] {
  type T[C] = Maybe[C]
  def bind[B](f: A => T[B]): T[B] = {
    this match {
      case Just(x) => f(x)
      case Nothing() => Nothing[B]()
    }
  }
  def ret[B](input: B) = Just(input)
  override def toString() = {
    this match {
      case Just(x) => "Just " + x.toString()
      case Nothing() => "Nothing"
    }
  }
  def fmap[C](f: A => C): T[C] = {
    this match {
      case Just(x) => ret(f(x))
      case Nothing() => Nothing[C]()
    }
  }
}
case class Just[A](x: A) extends Maybe[A]
case class Nothing[A]() extends Maybe[A]

abstract class Functor[A] {
  type T[B] <: Functor[B]
  def fmap[C](f: A => C): T[C]
}
