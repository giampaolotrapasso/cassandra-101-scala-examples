package phantom

import com.giampaolotrapasso.cassandra101.models.Book

import scala.concurrent.ExecutionContext.Implicits.global

class BasicOperationsTest extends TestSuite {

  "A BookDatabase" should "insert correctly a book" in {

    val book = Book(
      author = "Brian W. Kernighan",
      title = "The C Programming Language",
      published = 1978,
      born = 1942
    )

    val result = for {
      insert <- TestBookDatabase.insertBook(book)
      select <- TestBookDatabase.books.selectByAuthor("Brian W. Kernighan")
    } yield (insert, select)

    val maybeSelect: Option[Book] = result.futureValue._2.headOption

    maybeSelect.isDefined should equal(true)
    maybeSelect.get.author should equal(book.author)
    maybeSelect.get.title should equal(book.title)

  }

}