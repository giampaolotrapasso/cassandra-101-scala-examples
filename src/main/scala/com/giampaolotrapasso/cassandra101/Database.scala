package com.giampaolotrapasso.cassandra101

import com.giampaolotrapasso.cassandra101.models._
import com.websudos.phantom.connectors.KeySpaceDef
import com.websudos.phantom.db.DatabaseImpl
import com.websudos.phantom.dsl._

import scala.concurrent.Future

class Database(val keySpaceConnector: KeySpaceDef) extends DatabaseImpl(keySpaceConnector) {

  object books extends BooksDAO with keySpaceConnector.Connector

  object booksByYear extends BooksByYear with keySpaceConnector.Connector

  def insertBook(book: Book) = {

    val byYear = BookByYear(book.published, book.title)

    Batch.logged
      .add(books.insertNewStatement(book))
      .add(booksByYear.insertNewStatement(byYear))
      .future()
  }

  def selectByTitleWithFiltering(title: String): Future[List[Book]] = {
    books.select.allowFiltering().where(_.title eqs title).fetch()
  }
}

object BookDatabase extends Database(Config.keySpaceConnector)
