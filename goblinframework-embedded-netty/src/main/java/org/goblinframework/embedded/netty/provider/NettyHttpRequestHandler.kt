package org.goblinframework.embedded.netty.provider

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import org.goblinframework.embedded.core.setting.ServerSetting

class NettyHttpRequestHandler(private val setting: ServerSetting) : SimpleChannelInboundHandler<FullHttpRequest>() {

  override fun channelRead0(ctx: ChannelHandlerContext, msg: FullHttpRequest) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}