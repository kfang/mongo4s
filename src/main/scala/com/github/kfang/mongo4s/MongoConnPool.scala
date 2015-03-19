package com.github.kfang.mongo4s

import akka.actor.ActorSystem
import reactivemongo.api.{MongoConnectionOptions, MongoDriver}
import reactivemongo.core.nodeset.Authenticate

case class MongoConnPool(
  system: ActorSystem,
  nodes: Seq[String],
  connOptions: MongoConnectionOptions = MongoConnectionOptions(),
  auth: Seq[Authenticate] = Seq.empty,
  nbChannelsPerNode: Int = 10,
  name: Option[String] = None
) {
  val driver = MongoDriver(system)
  val connection = driver.connection(nodes, connOptions, auth, nbChannelsPerNode, name)
}
