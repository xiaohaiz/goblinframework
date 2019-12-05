package org.goblinframework.transport.client.channel

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.goblinframework.transport.core.codec.TransportMessage
import org.slf4j.LoggerFactory

class TransportClientChannelHandler(private val client: TransportClientImpl)
  : SimpleChannelInboundHandler<TransportMessage>() {

  companion object {
    private val logger = LoggerFactory.getLogger(TransportClientChannelHandler::class.java)
  }

  override fun channelRead0(ctx: ChannelHandlerContext, msg: TransportMessage) {
    client.onTransportMessage(msg)
  }

  override fun channelInactive(ctx: ChannelHandlerContext) {
    client.updateStateChannel(TransportClientChannel.DISCONNECTED)
  }

  override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable?) {
    logger.error("Exception caught at channel {}", ctx.channel(), cause)
    ctx.close()
  }
}