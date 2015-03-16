package com.github.kfang.mongo4s

import reactivemongo.api.{DefaultDB, FailoverStrategy}

trait MongoDatabase {
  def name: String
  def conn: MongoConnPool

  def failover: FailoverStrategy = FailoverStrategy()
  def db: DefaultDB = conn.connection.apply(name, failover)
}
