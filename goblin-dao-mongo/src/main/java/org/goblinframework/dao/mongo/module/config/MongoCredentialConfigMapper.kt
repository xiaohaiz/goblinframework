package org.goblinframework.dao.mongo.module.config

import java.io.Serializable

class MongoCredentialConfigMapper : Serializable {

  var database: String? = null
  var username: String? = null
  var password: String? = null

  companion object {
    private const val serialVersionUID = 8796887808758930695L
  }
}
