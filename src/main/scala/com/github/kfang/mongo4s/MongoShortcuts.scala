package com.github.kfang.mongo4s

import reactivemongo.bson._

import scala.util.Try

trait MongoShortcuts {

  //yeah.. checkstyle doesn't like but w/e, its convenient
  //possible fixes for checkstyle:
  // - make Bdoc a case class and provide an implicit conversion
  def Bdoc(inner: Producer[(String, BSONValue)]*): BSONDocument = BSONDocument(inner: _*)
  def Bdoc(inner: Traversable[(String, BSONValue)]): BSONDocument = BSONDocument(inner)
  def Bdoc(inner: Stream[Try[(String, BSONValue)]]): BSONDocument = BSONDocument(inner)

  def $in(seq: Seq[BSONValue]): (String, BSONValue) = "$in" -> BSONArray(seq)
  def $nin(seq: Seq[BSONValue]): (String, BSONValue) = "$nin" -> BSONArray(seq)

  def $inc(fieldAmount: (String, Double), more: (String, Double)*): (String, BSONValue) =
    "$inc" -> Bdoc(more.:+(fieldAmount).toMap.mapValues(BSONDouble.apply))

  def $mul(field: String, value: Double): (String, BSONValue) =
    "$mul" -> Bdoc(field -> value)

  def $rename(fieldNewName: (String, String), more: (String, String)*): (String, BSONValue) =
    "$rename" -> Bdoc(more.:+(fieldNewName).toMap.mapValues(BSONString.apply))

  def $setOnInsert(fieldVal: Producer[(String, BSONValue)], more: Producer[(String, BSONValue)]*): (String, BSONValue) =
    "$setOnInsert" -> Bdoc(more.:+(fieldVal): _*)

  def $set(fieldVal: Producer[(String, BSONValue)], more: Producer[(String, BSONValue)]*): (String, BSONValue) =
    "$set" -> Bdoc(more.:+(fieldVal): _*)

  def $unset(field: String, more: String*): (String, BSONValue) =
    "$unset" ->  Bdoc(more.:+(field).map(_ -> BSONString("")))

  def $min(fieldVal: Producer[(String, BSONValue)], more: Producer[(String, BSONValue)]*): (String, BSONValue) =
    "$min" -> Bdoc(more.:+(fieldVal): _*)

  def $max(fieldVal: Producer[(String, BSONValue)], more: Producer[(String, BSONValue)]*): (String, BSONValue) =
    "$max" -> Bdoc(more.:+(fieldVal): _*)

  def $currentDate(field: String, asDate: Boolean = true): (String, BSONValue) = asDate match {
    case true  => "$currentDate" -> Bdoc(field -> Bdoc("$type" -> "date"))
    case false => "$currentDate" -> Bdoc(field -> Bdoc("$type" -> "timestamp"))
  }

}

object MongoShortcuts extends MongoShortcuts
