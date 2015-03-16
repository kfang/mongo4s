package com.github.kfang.mongo4s.commands

import reactivemongo.bson.BSONDocument

case class FindQuery(
  sel: BSONDocument = BSONDocument()
)

trait FindDsl {
  def find(selector: BSONDocument = BSONDocument()): FindQuery = FindQuery(sel = selector)
}

object FindDsl extends FindDsl
