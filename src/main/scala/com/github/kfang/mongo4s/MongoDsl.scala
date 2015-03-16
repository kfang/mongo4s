package com.github.kfang.mongo4s

import com.github.kfang.mongo4s.commands.{FindDsl, CountDsl}

trait MongoDsl
  extends CountDsl
  with FindDsl

object MongoDsl extends MongoDsl
