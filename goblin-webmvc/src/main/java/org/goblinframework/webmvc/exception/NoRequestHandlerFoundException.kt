package org.goblinframework.webmvc.exception

import javax.servlet.ServletException

class NoRequestHandlerFoundException : ServletException {

  constructor() {}

  constructor(message: String) : super(message) {}

  constructor(message: String, rootCause: Throwable) : super(message, rootCause) {}

  constructor(rootCause: Throwable) : super(rootCause) {}

  companion object {
    private const val serialVersionUID = -1169664951409346739L
  }
}
