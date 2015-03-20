package com.github.kfang.mongo4s.commands

import reactivemongo.bson.BSONDocument
import reactivemongo.core.commands.GetLastError

case class InsertQuery(doc: BSONDocument, writeConcern: GetLastError = GetLastError()){
  def writeConcern(gle: GetLastError): InsertQuery = this.copy(writeConcern = gle)
}

case class InsertModelQuery[M](m: M, writeConcern: GetLastError = GetLastError()){
  def writeConcern(gle: GetLastError): InsertModelQuery[M] = this.copy(writeConcern = gle)
}


trait InsertDsl {

  def insert(doc: BSONDocument): InsertQuery = InsertQuery(doc = doc)
  def insert[M](m: M): InsertModelQuery[M] = InsertModelQuery[M](m = m)

  class InsertExpectsDocument {
    def doc(doc: BSONDocument): InsertQuery = insert(doc)
    def model[M](m: M): InsertModelQuery[M] = insert(m)
  }

  def insert: InsertExpectsDocument = new InsertExpectsDocument()

  //For the CRUD people
  def create(doc: BSONDocument): InsertQuery = insert(doc)
  def create[M](m: M): InsertModelQuery[M] = insert(m)
  def create: InsertExpectsDocument = new InsertExpectsDocument()

}

object InsertDsl extends InsertDsl
