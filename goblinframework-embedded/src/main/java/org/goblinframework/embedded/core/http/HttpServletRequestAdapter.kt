package org.goblinframework.embedded.core.http

import org.goblinframework.core.util.ReflectionUtils
import javax.servlet.http.HttpServletRequest

object HttpServletRequestAdapter {

  val adapter: HttpServletRequest by lazy {
    ReflectionUtils.createProxy(HttpServletRequest::class.java) { invocation ->
      val method = invocation.method
      if (ReflectionUtils.isToStringMethod(method)) {
        return@createProxy "HttpServletRequestAdapter"
      }
      throw UnsupportedOperationException("Unsupported HttpServletRequest method [${method.name}]")
    }
  }

}