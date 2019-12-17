package org.goblinframework.example.embedded.interceptor

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.example.embedded.utils.AuthUtils
import org.goblinframework.webmvc.handler.RequestHandler
import org.goblinframework.webmvc.interceptor.AbstractInterceptor
import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse
import javax.servlet.http.HttpServletResponse

@Singleton
class SessionAuthInterceptor private constructor() : AbstractInterceptor() {

  companion object {
    @JvmField val INSTANCE = SessionAuthInterceptor()
  }

  override fun preHandle(request: GoblinServletRequest, response: GoblinServletResponse, handler: RequestHandler): Boolean {
    if (!request.servletRequest.requestURI.startsWith("/user")) {
      return true
    }
    val session = request.servletRequest.getParameter("session_key")
    if (session == null) {
      response.sendTextResponse(HttpServletResponse.SC_BAD_REQUEST, "Please login first")
      return false
    }
    val userInfoMapper = AuthUtils.decodeSession(session)
    if (userInfoMapper == null) {
      response.sendTextResponse(HttpServletResponse.SC_BAD_REQUEST, "Please login first")
      return false
    }
    request.servletRequest.setAttribute("userInfo", userInfoMapper)
    return true
  }
}