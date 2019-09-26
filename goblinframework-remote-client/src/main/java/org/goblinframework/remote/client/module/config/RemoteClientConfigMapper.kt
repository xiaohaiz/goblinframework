package org.goblinframework.remote.client.module.config

import java.io.Serializable

class RemoteClientConfigMapper : Serializable {

  companion object {
    private const val serialVersionUID = 2349386765518738752L
  }

  var registry: String? = null

}
