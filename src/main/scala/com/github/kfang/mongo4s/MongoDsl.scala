package com.github.kfang.mongo4s

import com.github.kfang.mongo4s.commands.{InsertDsl, FindDsl, CountDsl}

trait MongoDsl
  extends CountDsl
  with FindDsl
  with InsertDsl

object MongoDsl extends MongoDsl
