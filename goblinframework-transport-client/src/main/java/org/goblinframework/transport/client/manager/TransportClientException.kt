package org.goblinframework.transport.client.manager

import org.goblinframework.api.common.GoblinException

class TransportClientException : GoblinException {

  constructor(message: String?, cause: Throwable?) : super(message, cause) {}

  constructor(cause: Throwable?) : super(cause) {}

  companion object {
    private const val serialVersionUID = -5663927824974760642L
  }
}
