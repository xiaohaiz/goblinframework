package org.goblinframework.webmvc.exception

import javax.servlet.ServletException

class RequestParamMissingException : ServletException {

  constructor() {}

  constructor(message: String) : super(message) {}

  constructor(message: String, rootCause: Throwable) : super(message, rootCause) {}

  constructor(rootCause: Throwable) : super(rootCause) {}

  companion object {
    private const val serialVersionUID = -7071642243894055675L
  }
}
