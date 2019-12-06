package org.goblinframework.embedded.core.servlet

import org.goblinframework.core.util.ProxyUtils
import org.goblinframework.core.util.ReflectionUtils
import javax.servlet.http.HttpServletResponse

object HttpServletResponseAdapter {

  val adapter: HttpServletResponse by lazy {
    ProxyUtils.createInterfaceProxy(HttpServletResponse::class.java) { invocation ->
      val method = invocation.method
      if (ReflectionUtils.isToStringMethod(method)) {
        return@createInterfaceProxy "HttpServletResponseAdapter"
      }
      throw UnsupportedOperationException("Unsupported HttpServletResponse method [${method.name}]")
    }
  }

}