package com.giampaolotrapasso.cassandra101.models

import com.datastax.driver.core.{ ResultSet, Row }
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._

import scala.concurrent.Future

case class Book(
  author: String,
  born: Int,
  title: String,
  published: Int,
  pages: Option[Int] = None
)

class BooksTable extends CassandraTable[BooksDAO, Book] {

  override val tableName = "books"

  object author extends StringColumn(this) with PartitionKey[String]

  object born extends IntColumn(this) with StaticColumn[Int]

  object title extends StringColumn(this) with PrimaryKey[String]

  object published extends IntColumn(this)

  object pages extends OptionalIntColumn(this)

  override def fromRow(row: Row): Book = {
    Book(
      author = author(row),
      born = born(row),
      title = title(row),
      published = published(row),
      pages = pages(row)
    )
  }

}

abstract class BooksDAO extends BooksTable with RootConnector {

  def insertNewStatement(book: Book) = {
    insert
      .value(_.author, book.author)
      .value(_.born, book.born)
      .value(_.title, book.title)
      .value(_.published, book.published)
  }

  def insertNew(book: Book): Future[ResultSet] = insertNewStatement(book).future()

  def selectByAuthor(author: String): Future[List[Book]] = {
    select.where(_.author eqs author).fetch()
  }

  def updateBook(author: String, book: String, published: Int, pages: Option[Int]): Future[ResultSet] = {
    update
      .where(_.author eqs author).and(_.title eqs book)
      .modify(_.published setTo published).and(_.pages setTo pages).future()
  }

}

