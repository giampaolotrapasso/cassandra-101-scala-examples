package com.giampaolotrapasso.cassandra101

import com.websudos.phantom.connectors.{ ContactPoint, KeySpace, KeySpaceBuilder, KeySpaceDef }

object Config {

  val keySpace = KeySpace("books")
  val contactPoint: KeySpaceBuilder = ContactPoint(host = "localhost", port = ContactPoint.DefaultPorts.live)
  val keySpaceConnector: KeySpaceDef = contactPoint.keySpace(keySpace.name)

}