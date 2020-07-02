package org.goblinframework.embedded.netty.server

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

class ReaderIdleConnectionHandler : ChannelDuplexHandler() {

  override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any?) {
    if (evt is IdleStateEvent) {
      if (evt.state() === IdleState.READER_IDLE) {
        ctx.close()
      }
    }
  }
}