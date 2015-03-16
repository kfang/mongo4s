package com.github.kfang.mongo4s

import reactivemongo.api.{DefaultDB, FailoverStrategy}

import scala.concurrent.ExecutionContext

trait MongoDatabase {
  def name: String
  def conn: MongoConnPool

  implicit val ec: ExecutionContext = conn.system.dispatcher
  def failover: FailoverStrategy = FailoverStrategy()
  def db: DefaultDB = conn.connection.apply(name, failover)
}
