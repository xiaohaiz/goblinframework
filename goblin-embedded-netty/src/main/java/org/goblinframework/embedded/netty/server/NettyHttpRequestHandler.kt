package org.goblinframework.embedded.netty.server

import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.QueryStringDecoder
import org.goblinframework.core.util.HttpUtils
import org.goblinframework.embedded.setting.ServerSetting
import org.goblinframework.webmvc.servlet.GoblinServletRequest
import org.goblinframework.webmvc.servlet.GoblinServletResponse
import org.springframework.http.MediaType
import java.util.concurrent.ThreadPoolExecutor
import javax.servlet.http.HttpServletResponse

class NettyHttpRequestHandler(private val setting: ServerSetting,
                              private val executor: ThreadPoolExecutor) : SimpleChannelInboundHandler<FullHttpRequest>() {

  override fun channelRead0(ctx: ChannelHandlerContext, msg: FullHttpRequest) {
    val requestBody = if (msg.content().isReadable) {
      ByteBufUtil.getBytes(msg.content())
    } else {
      ByteArray(0)
    }
    executor.submit { onRequest(ctx, msg, requestBody) }
  }

  override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
    ctx?.close()
  }

  private fun onRequest(ctx: ChannelHandlerContext, msg: FullHttpRequest, requestBody: ByteArray) {
    val response = NettyHttpServletResponse(ctx)

    val decoder = QueryStringDecoder(msg.uri(), Charsets.UTF_8)
    val rawPath = HttpUtils.compactContinuousSlashes(decoder.path())

    val parsed = setting.handlerSettings().lookupContextPath(rawPath)
    val contextPath = parsed.left
    val handlerSetting = setting.handlerSettings()[contextPath]
    if (handlerSetting == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unrecognized contextPath specified")
      response.close()
      return
    }

    val handler = handlerSetting.servletHandler()
    val path = handler.transformLookupPath(parsed.right)
    if (HttpUtils.isMalformedPath(path)) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "Malformed request path specified")
      response.close()
      return
    }

    val request = NettyHttpServletRequest(ctx, msg, requestBody, decoder, handlerSetting.contextPath(), path)
    try {
      handler.handle(GoblinServletRequest(request), GoblinServletResponse(response))
    } catch (ex: Exception) {
      response.resetBuffer()
      response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
      response.contentType = MediaType(MediaType.TEXT_PLAIN, Charsets.UTF_8).toString()
      ex.printStackTrace(response.writer)
    } finally {
      response.close()
    }
  }
}