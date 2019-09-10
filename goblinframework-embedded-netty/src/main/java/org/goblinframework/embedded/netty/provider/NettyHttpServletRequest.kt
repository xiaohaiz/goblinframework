package org.goblinframework.embedded.netty.provider

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.QueryStringDecoder
import org.goblinframework.core.util.StringUtils
import org.goblinframework.embedded.core.servlet.AbstractHttpServletRequest
import org.springframework.util.LinkedMultiValueMap

class NettyHttpServletRequest(private val ctx: ChannelHandlerContext,
                              private val request: FullHttpRequest,
                              decoder: QueryStringDecoder)
  : AbstractHttpServletRequest() {

  private val inputStream = NettyServletInputStream(request)
  private val parameters = LinkedMultiValueMap<String, String>()
  private val query: String

  init {
    val uri = decoder.uri()
    query = if (StringUtils.contains(uri, "?")) {
      StringUtils.substringAfter(uri, "?")
    } else {
      StringUtils.EMPTY
    }
  }
}