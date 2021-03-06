package com.github.kfang.mongo4s

import com.github.kfang.mongo4s.commands._
import play.api.libs.iteratee.Enumerator
import reactivemongo.api.{FailoverStrategy, DefaultDB}
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONArray, BSONDocumentWriter, BSONDocument, BSONDocumentReader}
import reactivemongo.core.commands.{LastError, Count}

import scala.concurrent.{ExecutionContext, Future}

class MongoCollection[M](db: DefaultDB, name: String, failover: FailoverStrategy = FailoverStrategy())
                        (implicit reader: BSONDocumentReader[M], writer: BSONDocumentWriter[M])
  extends BSONCollection(db, name, failover){

  implicit val ec: ExecutionContext = db.connection.actorSystem.dispatcher

  def execute(query: CountQuery): Future[Int] = {
    db.command(Count(name, Some(query.selector)), query.readPref)
  }

  def execute(query: DistinctQuery): Future[BSONArray] = {
    db.command(Distinct(name, query.field, query.sel), query.readPref)
  }

  /** Default FindQuery **/
  def execute(query: FindQuery): Enumerator[M] = {
    find(query.sel).sort(query.sort).options(query.opts).cursor[M](query.readPref).enumerate(query.maxDocs, query.stopOnError)
  }

  /** FindOneQuery, return just one **/
  def execute(query: FindOneQuery): Future[Option[M]] = {
    find(query.sel).sort(query.sort).options(query.opts).one[M]
  }

  /** FindListQuery, return a List **/
  def execute(query: FindListQuery): Future[List[M]] = {
    find(query.sel).sort(query.sort).options(query.opts).cursor[M](query.readPref).collect[List](query.maxDocs, query.stopOnError)
  }

  /** FindBulkQuery, return an Iterator **/
  def execute(query: FindBulkQuery): Enumerator[Iterator[M]] = {
    find(query.sel).sort(query.sort).options(query.opts).cursor[M](query.readPref).enumerateBulks(query.maxDocs, query.stopOnError)
  }

  /** FindProjectionQuery, return documents that have been projected **/
  def execute(query: FindProjectionQuery): Enumerator[BSONDocument] = {
    find(query.sel, query.proj).sort(query.sort).options(query.opts).cursor[BSONDocument](query.readPref).enumerate(query.maxDocs, query.stopOnError)
  }

  /** FindProjectionBulkQuery, return documents that have been projected in bulks **/
  def execute(query: FindProjectionBulkQuery): Enumerator[Iterator[BSONDocument]] = {
    find(query.sel, query.proj).sort(query.sort).options(query.opts).cursor[BSONDocument](query.readPref).enumerateBulks(query.maxDocs, query.stopOnError)
  }

  /** FindProjectionListQuery, return documents that have been projected in a List **/
  def execute(query: FindProjectionListQuery): Future[List[BSONDocument]] = {
    find(query.sel, query.proj).sort(query.sort).options(query.opts).cursor[BSONDocument](query.readPref).collect[List](query.maxDocs, query.stopOnError)
  }

  /** InsertQuery **/
  def execute(query: InsertQuery): Future[LastError] = {
    insert(query.doc, query.writeConcern)
  }

  /** InsertModelQuery **/
  def execute(query: InsertModelQuery[M]): Future[LastError] = {
    insert[M](query.m, query.writeConcern)
  }

  /** RemoveQuery **/
  def execute(query: RemoveQuery): Future[LastError] = {
    remove(query.sel, query.writeConcern, query.firstMatchOnly)
  }

  /** RemoveModelQuery **/
  def execute(query: RemoveModelQuery[M]): Future[LastError] = {
    remove(query.m, query.writeConcern, query.firstMatchOnly)
  }

}
