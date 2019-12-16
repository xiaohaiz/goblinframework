package org.goblinframework.core.util

import org.goblinframework.api.core.GoblinException

class GoblinNetworkException : GoblinException {

  constructor() {}
  constructor(message: String?) : super(message) {}
  constructor(message: String?, cause: Throwable?) : super(message, cause) {}
  constructor(cause: Throwable?) : super(cause) {}

  companion object {
    private const val serialVersionUID = 2387665840385166671L
  }

}