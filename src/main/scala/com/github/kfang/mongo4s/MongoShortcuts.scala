package com.github.kfang.mongo4s

import reactivemongo.bson._

import scala.util.Try

trait MongoShortcuts {

  def BDoc(inner: Producer[(String, BSONValue)]*): BSONDocument = BSONDocument(inner: _*)
  def BDoc(inner: Traversable[(String, BSONValue)]): BSONDocument = BSONDocument(inner)
  def BDoc(inner: Stream[Try[(String, BSONValue)]]): BSONDocument = BSONDocument(inner)

  def $in(seq: Seq[BSONValue]): BSONDocument = BDoc("$in" -> BSONArray(seq))
  def $nin(seq: Seq[BSONValue]): BSONDocument = BDoc("$nin" -> BSONArray(seq))

  def $inc(fieldAmount: (String, Double), more: (String, Double)*): BSONDocument =
    BDoc("$inc" -> BDoc(more.:+(fieldAmount).toMap.mapValues(BSONDouble.apply)))

  def $mul(field: String, value: Double): BSONDocument =
    BDoc("$mul" -> BDoc(field -> value))

  def $rename(fieldNewName: (String, String), more: (String, String)*): BSONDocument =
    BDoc("$rename" -> BDoc(more.:+(fieldNewName).toMap.mapValues(BSONString.apply)))

  def $setOnInsert(fieldVal: Producer[(String, BSONValue)], more: Producer[(String, BSONValue)]*): BSONDocument =
    BDoc("$setOnInsert" -> BDoc(more.:+(fieldVal): _*))

  def $set(fieldVal: Producer[(String, BSONValue)], more: Producer[(String, BSONValue)]*): BSONDocument =
    BDoc("$set" -> BDoc(more.:+(fieldVal): _*))

  def $unset(field: String, more: String*): BSONDocument =
    BDoc("$unset" ->  BDoc(more.:+(field).map(_ -> BSONString(""))))

  def $min(fieldVal: Producer[(String, BSONValue)], more: Producer[(String, BSONValue)]*): BSONDocument =
    BDoc("$min" -> BDoc(more.:+(fieldVal): _*))

  def $max(fieldVal: Producer[(String, BSONValue)], more: Producer[(String, BSONValue)]*): BSONDocument =
    BDoc("$max" -> BDoc(more.:+(fieldVal): _*))

  def $currentDate(field: String, asDate: Boolean = true): BSONDocument = asDate match {
    case true  => BDoc("$currentDate" -> BDoc(field -> BDoc("$type" -> "date")))
    case false => BDoc("$currentDate" -> BDoc(field -> BDoc("$type" -> "timestamp")))
  }

}

object MongoShortcuts extends MongoShortcuts
