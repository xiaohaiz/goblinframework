package org.goblinframework.embedded.handler

import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import javax.servlet.ServletException

interface ServletHandler {

  fun transformLookupPath(path: String): String

  @Throws(ServletException::class)
  fun handle(request: ServletRequest, response: ServletResponse)


}
