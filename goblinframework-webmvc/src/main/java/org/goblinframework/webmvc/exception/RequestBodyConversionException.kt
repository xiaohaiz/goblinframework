package org.goblinframework.webmvc.exception

import javax.servlet.ServletException

class RequestBodyConversionException : ServletException {

  constructor() {}

  constructor(message: String) : super(message) {}

  constructor(message: String, rootCause: Throwable) : super(message, rootCause) {}

  constructor(rootCause: Throwable) : super(rootCause) {}

  companion object {
    private const val serialVersionUID = -1099262107769357328L
  }
}
