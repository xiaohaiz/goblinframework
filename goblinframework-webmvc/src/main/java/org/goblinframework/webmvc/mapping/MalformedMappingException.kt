package org.goblinframework.webmvc.mapping

import org.goblinframework.core.exception.GoblinException

class MalformedMappingException : GoblinException {

  constructor() {}

  constructor(message: String) : super(message) {}

  constructor(message: String, cause: Throwable) : super(message, cause) {}

  constructor(cause: Throwable) : super(cause) {}

  companion object {
    private const val serialVersionUID = 6206295305313619407L
  }
}
