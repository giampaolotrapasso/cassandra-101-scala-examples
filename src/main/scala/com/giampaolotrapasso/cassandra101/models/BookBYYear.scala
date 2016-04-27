package com.giampaolotrapasso.cassandra101.models

import com.datastax.driver.core.Row
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._

import scala.concurrent.Future

case class BookByYear(
  year: Int,
  book: String
)

class BooksByYearTable extends CassandraTable[BooksByYearTable, BookByYear] {

  override val tableName = "books_by_year"

  object year extends IntColumn(this) with PartitionKey[Int]

  object book extends StringColumn(this) with ClusteringOrder[String] with Ascending

  override def fromRow(row: Row): BookByYear = {
    BookByYear(
      year = year(row),
      book = book(row)
    )
  }
}

abstract class BooksByYearDAO extends BooksByYearTable with RootConnector {

  def insertNewStatement(bookByYear: BookByYear) = {
    insert
      .value(_.year, bookByYear.year)
      .value(_.book, bookByYear.book)
  }

  def insertNew(postByAuthor: BookByYear): Future[ResultSet] = insertNewStatement(postByAuthor).future

  def selectByYear(year: Int): Future[List[BookByYear]] = {
    select.where(_.year eqs year).fetch()
  }

}

