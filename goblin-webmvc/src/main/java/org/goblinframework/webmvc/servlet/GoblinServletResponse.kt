package org.goblinframework.webmvc.servlet

import org.goblinframework.core.util.HttpUtils
import org.goblinframework.core.util.StringUtils
import org.springframework.http.MediaType
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.ui.Model
import javax.servlet.http.HttpServletResponse

class GoblinServletResponse(response: HttpServletResponse) : ServletServerHttpResponse(response) {

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

  fun sendTextResponse(code: Int, text: String) {
    headers.contentLength = text.length.toLong()
    headers.contentType = MediaType.TEXT_PLAIN
    servletResponse.status = code
    servletResponse.writer.println(text)

    flush()
  }
}
