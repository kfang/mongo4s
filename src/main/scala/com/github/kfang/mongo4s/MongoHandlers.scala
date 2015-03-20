package com.github.kfang.mongo4s

import org.joda.time.DateTime
import reactivemongo.bson._

trait MongoHandlers {

  implicit object DateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
    override def write(t: DateTime): BSONDateTime = BSONDateTime(t.getMillis)
    override def read(bson: BSONDateTime): DateTime = new DateTime(bson.value)
  }

  implicit object MapStringHandler extends BSONHandler[BSONDocument, Map[String, BSONValue]] {
    override def write(t: Map[String, BSONValue]): BSONDocument = BSONDocument(t)
    override def read(bson: BSONDocument): Map[String, BSONValue] = bson.elements.toMap
  }

}

object MongoHandlers extends MongoHandlers
