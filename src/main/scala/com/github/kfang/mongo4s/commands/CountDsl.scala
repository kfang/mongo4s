package com.github.kfang.mongo4s.commands

import reactivemongo.api.ReadPreference
import reactivemongo.bson.{BSONValue, Producer, BSONDocument}

case class CountQuery(
  selector: BSONDocument = BSONDocument(),
  readPref: ReadPreference = ReadPreference.primary
){
  def readPref(rp: ReadPreference): CountQuery = this.copy(readPref = rp)
}

trait CountDsl {
  def count(selector: BSONDocument): CountQuery = CountQuery(selector)
  def count(selector: Producer[(String, BSONValue)]*): CountQuery = CountQuery(BSONDocument(selector: _*))
}

object CountDsl extends CountDsl
