package com.giampaolotrapasso.cassandra101

import com.datastax.driver.core.{ ResultSet, Session }
import com.giampaolotrapasso.cassandra101.models.Book
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }
import scala.util.{ Failure, Success, Try }

/*
  to start a C* cluster do
  brew install ccm
  sudo ifconfig lo0 alias 127.0.0.2
  sudo ifconfig lo0 alias 127.0.0.3
  ccm create -v 2.1.5 -n 3 clusterTest
  ccm start
*/

object SimpleRun extends App {

  def doStuff(implicit session: Session) = {

    implicit val space = BookDatabase.space

    Await.result(BookDatabase.autocreate().future, 10 seconds)

    val book1 = Book(
      author = "Melville",
      born = 1819,
      title = "White-Jacket",
      published = 1850
    )

    val book2 = book1.copy(title = "Moby Dick", published = 1851)

    val book3 = Book(
      author = "Hemingway",
      born = 1899,
      title = "For Whom the Bell tolls",
      published = 1940
    )

    val book4 = Book(
      author = "Dino Buzzati",
      born = 1906,
      title = "Il deserto dei tartari",
      published = 1940
    )

    val books: List[Future[ResultSet]] = List(book1, book2, book3, book4).map(p => BookDatabase.insertBook(p))
    val operations: Future[List[ResultSet]] = Future.sequence(books)

    Await.result(operations, 10.seconds)

    val listOfMelville: Future[List[Book]] = BookDatabase.books.selectByAuthor("Melville")

    listOfMelville.onComplete {
      case Success(list) => println(s"${list.size} books by Melville ")
      case Failure(x) => println(x)
    }

    Await.result(listOfMelville, 10.seconds)

    val booksOf1940 = BookDatabase.booksByYear.selectByYear(1940)

    booksOf1940.onComplete {
      case Success(list) => println(s"${list.size} books in 1940")
      case Failure(x) => println(x)
    }

    Await.ready(booksOf1940, 3.seconds)

    println("Sample ended")
    System.exit(0)
  }

  val connect: Try[Session] = Try(BookDatabase.session)

  connect match {
    case Success(session) => doStuff(session)
    case Failure(error) => println(s"KO: ${error}")
  }

}
