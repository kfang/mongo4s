package com.github.kfang.mongo4s

import com.github.kfang.mongo4s.commands.CountQuery
import reactivemongo.api.{FailoverStrategy, DefaultDB}
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.core.commands.Count

import scala.concurrent.Future

class MongoCollection(db: DefaultDB, name: String, failover: FailoverStrategy = FailoverStrategy())
  extends BSONCollection(db, name, failover){

  def execute(query: CountQuery): Future[Int] = {
    db.command(Count(name, Some(query.selector)), query.readPref)
  }

}
