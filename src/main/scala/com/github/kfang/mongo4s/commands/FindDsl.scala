package com.github.kfang.mongo4s.commands

import reactivemongo.api.{QueryOpts, ReadPreference}
import reactivemongo.bson._

case class FindQuery(
  sel: BSONDocument = BSONDocument(),
  readPref: ReadPreference = ReadPreference.Primary,
  maxDocs: Int = Int.MaxValue,
  stopOnError: Boolean = false,
  sort: BSONDocument = BSONDocument(),
  opts: QueryOpts = QueryOpts()
){
  def readPref(rp: ReadPreference): FindQuery = this.copy(readPref = rp)
  def limit(i: Int): FindQuery = this.copy(maxDocs = i)
  def stopOnError(b: Boolean) = this.copy(stopOnError = b)
  def opts(opt: QueryOpts): FindQuery = this.copy(opts = opt)
  def batchSize(i: Int): FindQuery = this.copy(opts = opts.copy(batchSizeN = i))
  def skip(i: Int): FindQuery = this.copy(opts = opts.copy(skipN = i))
  def sort(s: BSONDocument): FindQuery = this.copy(sort = s)
  def sort(s: Producer[(String, BSONValue)]*): FindQuery = this.copy(sort = BSONDocument(s: _*))

  def one: FindOneQuery = FindOneQuery(sel, readPref, maxDocs, stopOnError, sort, opts)
  def asList: FindListQuery = FindListQuery(sel, readPref, maxDocs, stopOnError, sort, opts)
  def asBulk: FindBulkQuery = FindBulkQuery(sel, readPref, maxDocs, stopOnError, sort, opts)
  def asBulk(bulkSize: Int): FindBulkQuery = FindBulkQuery(sel, readPref, maxDocs, stopOnError, sort, opts.copy(batchSizeN = bulkSize))

  def project(bs: BSONDocument): FindProjectionQuery = FindProjectionQuery(sel, bs, readPref, maxDocs, stopOnError, sort, opts)
  def project(pr: Producer[(String, BSONValue)]*): FindProjectionQuery =
    FindProjectionQuery(sel, BSONDocument(pr: _*), readPref, maxDocs, stopOnError, sort, opts)
}

case class FindOneQuery(
  sel: BSONDocument,
  readPref: ReadPreference,
  maxDocs: Int,
  stopOnError: Boolean,
  sort: BSONDocument,
  opts: QueryOpts
)

case class FindListQuery(
  sel: BSONDocument,
  readPref: ReadPreference,
  maxDocs: Int,
  stopOnError: Boolean,
  sort: BSONDocument,
  opts: QueryOpts
)

case class FindBulkQuery(
  sel: BSONDocument,
  readPref: ReadPreference,
  maxDocs: Int,
  stopOnError: Boolean,
  sort: BSONDocument,
  opts: QueryOpts
)

case class FindProjectionQuery(
  sel: BSONDocument,
  proj: BSONDocument,
  readPref: ReadPreference,
  maxDocs: Int,
  stopOnError: Boolean,
  sort: BSONDocument,
  opts: QueryOpts
){
  def asList: FindProjectionListQuery =
    FindProjectionListQuery(sel, proj, readPref, maxDocs, stopOnError, sort, opts)
  def asBulk: FindProjectionBulkQuery =
    FindProjectionBulkQuery(sel, proj, readPref, maxDocs, stopOnError, sort, opts)
  def asBulk(bulkSize: Int): FindProjectionBulkQuery =
    FindProjectionBulkQuery(sel, proj, readPref, maxDocs, stopOnError, sort, opts.copy(batchSizeN = bulkSize))
}

case class FindProjectionListQuery(
  sel: BSONDocument,
  proj: BSONDocument,
  readPref: ReadPreference,
  maxDocs: Int,
  stopOnError: Boolean,
  sort: BSONDocument,
  opts: QueryOpts
)

case class FindProjectionBulkQuery(
  sel: BSONDocument,
  proj: BSONDocument,
  readPref: ReadPreference,
  maxDocs: Int,
  stopOnError: Boolean,
  sort: BSONDocument,
  opts: QueryOpts
)

trait FindDsl {

  def find(sel: BSONDocument = BSONDocument()): FindQuery = FindQuery(sel = sel)
  def find(sel: Producer[(String, BSONValue)]*): FindQuery = FindQuery(sel = BSONDocument(sel: _*))

  class FindExpectsId {
    //selection by id
    def id(s: BSONValue): FindQuery = FindQuery(sel = BSONDocument("_id" -> s))
    def id(s: String): FindQuery = id(BSONString(s))

    //back references if you're a stickler for consistency
    def sel(s: BSONDocument): FindQuery = find(s)
    def sel(s: Producer[(String, BSONValue)]*): FindQuery = find(s: _*)
  }

  def find: FindExpectsId = new FindExpectsId()

  //For the CRUD people
  def read(sel: BSONDocument = BSONDocument()): FindQuery = find(sel)
  def read(sel: Producer[(String, BSONValue)]*): FindQuery = find(sel: _*)
  def read: FindExpectsId = new FindExpectsId()
}

object FindDsl extends FindDsl


