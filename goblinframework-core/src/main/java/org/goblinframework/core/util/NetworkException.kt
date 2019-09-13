package org.goblinframework.core.util

import org.goblinframework.api.common.GoblinException

class NetworkException : GoblinException {

  constructor() {}

  constructor(message: String) : super(message) {}

  constructor(message: String, cause: Throwable) : super(message, cause) {}

  constructor(cause: Throwable) : super(cause) {}

  companion object {
    private const val serialVersionUID = 2985411115497915721L
  }
}
