package org.goblinframework.example.embedded.interceptor

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.example.embedded.utils.AuthUtils
import org.goblinframework.webmvc.handler.RequestHandler
import org.goblinframework.webmvc.interceptor.AbstractInterceptor
import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse
import javax.servlet.http.HttpServletResponse

@Singleton
class CookieAuthInterceptor private constructor() : AbstractInterceptor() {

  companion object {
    @JvmField val INSTANCE = CookieAuthInterceptor()
  }

  override fun preHandle(request: GoblinServletRequest, response: GoblinServletResponse, handler: RequestHandler): Boolean {
    val cookies = request.servletRequest.cookies
    val authCookie = cookies.firstOrNull { it.name == "auth_info" }
    if (authCookie == null) {
      response.sendTextResponse(HttpServletResponse.SC_BAD_REQUEST, "Please login first")
      return false
    }
    val userInfoMapper = AuthUtils.decodeCookie(authCookie.value)
    if (userInfoMapper == null) {
      response.sendTextResponse(HttpServletResponse.SC_BAD_REQUEST, "Please login first")
      return false
    }
    request.servletRequest.setAttribute("userInfo", userInfoMapper)
    return true
  }


}