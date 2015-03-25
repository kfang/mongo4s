package com.github.kfang.mongo4s

import com.github.kfang.mongo4s.commands._

trait MongoDsl
  extends MongoShortcuts
  with CountDsl
  with DistinctDsl
  with InsertDsl
  with FindDsl
  with RemoveDsl

object MongoDsl extends MongoDsl
