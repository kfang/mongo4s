package com.github.kfang.mongo4s.commands

import reactivemongo.bson.{BSONDocument, BSONArray}
import reactivemongo.core.commands.{BSONCommandResultMaker, CommandError, CommandResultMaker, Command}

case class Distinct(
  coll: String,
  field: String,
  sel: BSONDocument
) extends Command[BSONArray] {

  val ResultMaker: CommandResultMaker[BSONArray] = Distinct

  def makeDocuments: BSONDocument = BSONDocument(
    "distinct" -> coll,
    "key" -> field,
    "query" -> sel
  )

}

object Distinct extends BSONCommandResultMaker[BSONArray] {
   def apply(document: BSONDocument): Either[CommandError, BSONArray] = {
     CommandError.checkOk(document, name = None).toLeft(document.getAs[BSONArray]("values").get)
   }
}
