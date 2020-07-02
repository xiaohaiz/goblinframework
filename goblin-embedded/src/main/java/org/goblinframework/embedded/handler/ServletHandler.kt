package org.goblinframework.embedded.handler

import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse
import javax.servlet.ServletException

interface ServletHandler {

  fun transformLookupPath(path: String): String

  @Throws(ServletException::class)
  fun handle(request: GoblinServletRequest, response: GoblinServletResponse)


}
