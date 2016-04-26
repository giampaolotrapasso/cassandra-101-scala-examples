package phantom

import com.giampaolotrapasso.cassandra101.Database
import com.websudos.phantom.connectors.{ ContactPoint, KeySpaceDef }
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers }

import scala.concurrent.Await
import scala.concurrent.duration._

object TestConfig {
  val testConnector: KeySpaceDef = ContactPoint.embedded.keySpace("bookTest")
}

object TestBookDatabase extends Database(TestConfig.testConnector)

trait TestSuite extends FlatSpec with Matchers
    with ScalaFutures
    with BeforeAndAfterAll
    with TestConfig.testConnector.Connector {

  import scala.concurrent.ExecutionContext.Implicits.global

  override def beforeAll(): Unit = {
    super.beforeAll()
    Await.ready(TestBookDatabase.autocreate.future(), 4.seconds)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    Await.result(TestBookDatabase.autotruncate.future(), 10.seconds)
  }

}
