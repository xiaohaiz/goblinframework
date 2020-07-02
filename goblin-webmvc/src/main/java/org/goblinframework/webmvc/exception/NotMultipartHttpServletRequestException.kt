package org.goblinframework.webmvc.exception

import javax.servlet.ServletException

class NotMultipartHttpServletRequestException : ServletException {

  constructor() {}

  constructor(message: String) : super(message) {}

  constructor(message: String, rootCause: Throwable) : super(message, rootCause) {}

  constructor(rootCause: Throwable) : super(rootCause) {}

  companion object {
    private const val serialVersionUID = -4577008536135059215L
  }
}
