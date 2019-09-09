package org.goblinframework.webmvc.servlet

import org.goblinframework.core.util.StringUtils
import org.goblinframework.http.util.HttpUtils
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.ui.Model

import javax.servlet.http.HttpServletResponse

class GoblinServletServerHttpResponse(response: HttpServletResponse) : ServletServerHttpResponse(response) {

  fun sendRedirect(location: String, model: Model?) {
    var loc = location
    if (loc.startsWith("redirect:", true)) {
      loc = StringUtils.substringAfter(loc, ":")
    }
    val map = model?.asMap() ?: emptyMap()
    if (map.isNotEmpty()) {
      val qs = HttpUtils.buildQueryString(map)
      if (qs.isNotEmpty()) {
        loc = if (loc.contains("?")) "$loc&$qs" else "$loc?$qs"
      }
    }
    servletResponse.sendRedirect(loc)
  }

  fun resetBuffer() {
    servletResponse.resetBuffer()
  }
}
