package org.goblinframework.embedded.servlet

import org.goblinframework.core.util.ProxyUtils
import org.goblinframework.core.util.ReflectionUtils
import javax.servlet.http.HttpServletRequest

object HttpServletRequestAdapter {

  val adapter: HttpServletRequest by lazy {
    ProxyUtils.createInterfaceProxy(HttpServletRequest::class.java) { invocation ->
      val method = invocation.method
      if (ReflectionUtils.isToStringMethod(method)) {
        return@createInterfaceProxy "HttpServletRequestAdapter"
      }
      throw UnsupportedOperationException("Unsupported HttpServletRequest method [${method.name}]")
    }
  }

}