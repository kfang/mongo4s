package com.github.kfang.mongo4s

import org.joda.time.DateTime
import reactivemongo.bson._

trait MongoHandlers {

  implicit object DateTimeHandler extends BSONWriter[DateTime, BSONDateTime] with BSONReader[BSONDateTime, DateTime] {
    override def write(t: DateTime): BSONDateTime = BSONDateTime(t.getMillis)
    override def read(bson: BSONDateTime): DateTime = new DateTime(bson.value)
  }

}

object MongoHandlers extends MongoHandlers
