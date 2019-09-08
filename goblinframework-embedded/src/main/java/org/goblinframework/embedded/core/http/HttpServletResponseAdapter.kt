package org.goblinframework.embedded.core.http

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.ReflectionUtils
import org.springframework.aop.framework.ProxyFactory
import javax.servlet.http.HttpServletResponse

object HttpServletResponseAdapter {

  val adapter: HttpServletResponse by lazy {
    val interceptor = object : MethodInterceptor {
      override fun invoke(invocation: MethodInvocation): Any? {
        val method = invocation.method
        if (ReflectionUtils.isToStringMethod(method)) {
          return "HttpServletResponseAdapter"
        }
        throw UnsupportedOperationException("Unsupported HttpServletResponse method [${method.name}]")
      }
    }
    val proxyFactory = ProxyFactory()
    proxyFactory.setInterfaces(HttpServletResponse::class.java)
    proxyFactory.addAdvice(interceptor)
    proxyFactory.getProxy(ClassUtils.getDefaultClassLoader()) as HttpServletResponse
  }

}