package com.github.kfang.mongo4s.commands

import reactivemongo.bson.{BSONValue, Producer, BSONDocument}
import reactivemongo.core.commands.GetLastError

case class RemoveQuery(
  sel: BSONDocument,
  writeConcern: GetLastError = GetLastError(),
  firstMatchOnly: Boolean = true
){
  def writeConcern(gle: GetLastError): RemoveQuery = this.copy(writeConcern = gle)
  def firstMatchOnly(b: Boolean): RemoveQuery = this.copy(firstMatchOnly = b)
}

case class RemoveModelQuery[M](
  m: M,
  writeConcern: GetLastError = GetLastError(),
  firstMatchOnly: Boolean = true
){
  def writeConcern(gle: GetLastError): RemoveModelQuery[M] = this.copy(writeConcern = gle)
  def firstMatchOnly(b: Boolean): RemoveModelQuery[M] = this.copy(firstMatchOnly = b)
}

trait RemoveDsl {

  def remove(sel: BSONDocument): RemoveQuery = RemoveQuery(sel)
  def remove(sel: Producer[(String, BSONValue)]*): RemoveQuery = remove(BSONDocument(sel: _*))
  def remove[M](m: M): RemoveModelQuery[M] = RemoveModelQuery(m)

  class RemoveExpectsSel {
    def id(id: String): RemoveQuery = remove(BSONDocument("_id" -> id))
    def id(id: BSONValue): RemoveQuery = remove(BSONDocument("_id" -> id))
    def sel(s: BSONDocument): RemoveQuery = remove(s)
    def sel(s: Producer[(String, BSONValue)]*): RemoveQuery = remove(BSONDocument(s: _*))
    def model[M](m: M): RemoveModelQuery[M] = remove(m)
  }

  def remove: RemoveExpectsSel = new RemoveExpectsSel()


  //For the CRUD people
  def delete(sel: BSONDocument): RemoveQuery = remove(sel)
  def delete(sel: Producer[(String, BSONValue)]*): RemoveQuery = remove(BSONDocument(sel: _*))
  def delete[M](m: M): RemoveModelQuery[M] = RemoveModelQuery(m)
  def delete: RemoveExpectsSel = new RemoveExpectsSel()

}

object RemoveDsl extends RemoveDsl

