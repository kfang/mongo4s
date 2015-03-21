package com.github.kfang.mongo4s

import com.github.kfang.mongo4s.commands.{RemoveDsl, InsertDsl, FindDsl, CountDsl}

trait MongoDsl
  extends CountDsl
  with InsertDsl
  with FindDsl
  with RemoveDsl

object MongoDsl extends MongoDsl
