package com.github.kfang.mongo4s.commands

import reactivemongo.api.ReadPreference
import reactivemongo.bson.{BSONValue, Producer, BSONDocument}

case class DistinctQuery(
  field: String,
  sel: BSONDocument = BSONDocument(),
  readPref: ReadPreference = ReadPreference.Primary
){
  def readPref(rp: ReadPreference): DistinctQuery = this.copy(readPref = rp)
  def sel(doc: BSONDocument): DistinctQuery = this.copy(sel = doc)
  def sel(doc: Producer[(String, BSONValue)]*): DistinctQuery = this.copy(sel = BSONDocument(doc: _*))
}

trait DistinctDsl {
  def distinct(field: String): DistinctQuery = DistinctQuery(field)

  class DistinctExpectsField {
    def field(f: String): DistinctQuery = distinct(f)
  }

  def distinct: DistinctExpectsField = new DistinctExpectsField()
}

object DistinctDsl extends DistinctDsl
