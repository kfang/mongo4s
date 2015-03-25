package com.github.kfang.mongo4s

import reactivemongo.bson._

import scala.util.Try

/**
 * Obviously this is not going to be exhaustive.  If you want a shortcut added,
 * either file and issue or submit a pull request.  The community will thank you.
 */
trait MongoShortcuts {

  //yeah.. checkstyle doesn't like but w/e, its convenient
  //possible fixes for checkstyle:
  // - make Bdoc a case class and provide an implicit conversion
  def Bdoc(inner: Producer[(String, BSONValue)]*): BSONDocument = BSONDocument(inner: _*)
  def Bdoc(inner: Traversable[(String, BSONValue)]): BSONDocument = BSONDocument(inner)
  def Bdoc(inner: Stream[Try[(String, BSONValue)]]): BSONDocument = BSONDocument(inner)

  def $in(seq: Seq[BSONValue]): (String, BSONValue) = "$in" -> BSONArray(seq)
  def $nin(seq: Seq[BSONValue]): (String, BSONValue) = "$nin" -> BSONArray(seq)
  def $exists(b: Boolean): (String, BSONValue) = "$exists" -> BSONBoolean(value = b)

  /**********************************************************************************
   * Field Operators
   *********************************************************************************/

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

  /**********************************************************************************
    * Array Operators
    *********************************************************************************/

  def $addToSet(fieldVal: Producer[(String, BSONValue)], more: Producer[(String, BSONValue)]*): (String, BSONValue) =
    "$addToSet" -> Bdoc(more.:+(fieldVal): _*)

  def $pop(field: String, popFirst: Boolean = true): (String, BSONValue) = popFirst match {
    case true  => "$pop" -> Bdoc(field -> -1)
    case false => "$pop" -> Bdoc(field -> 1)
  }

  def $pull(field: String, criteria: BSONValue): (String, BSONValue) =
    "$pull" -> Bdoc(field -> criteria)

  def $pull[T](field: String, criteria: T)(implicit writer: BSONWriter[T, BSONValue]): (String, BSONValue) =
    "$pull" -> Bdoc(field -> writer.write(criteria))

  def $push(field: String, value: BSONValue): (String, BSONValue) =
    "$push" -> Bdoc(field -> value)

  def $push[T](field: String, value: T)(implicit writer: BSONDocumentWriter[T]): (String, BSONValue) =
    "$push" -> Bdoc(field -> writer.write(value))
}

object MongoShortcuts extends MongoShortcuts
