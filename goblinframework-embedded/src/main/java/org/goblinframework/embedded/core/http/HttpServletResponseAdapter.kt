package org.goblinframework.embedded.core.http

import org.goblinframework.core.util.ReflectionUtils
import javax.servlet.http.HttpServletResponse

object HttpServletResponseAdapter {

  val adapter: HttpServletResponse by lazy {
    ReflectionUtils.createProxy(HttpServletResponse::class.java) { invocation ->
      val method = invocation.method
      if (ReflectionUtils.isToStringMethod(method)) {
        return@createProxy "HttpServletResponseAdapter"
      }
      throw UnsupportedOperationException("Unsupported HttpServletResponse method [${method.name}]")
    }
  }

}