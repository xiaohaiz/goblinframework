package org.goblinframework.dao.mongo.module.monitor.translator

import com.mongodb.WriteConcern

internal fun translateWriteConcern(writeConcern: WriteConcern?): String? {
  return writeConcern?.run { "$this}" }
}