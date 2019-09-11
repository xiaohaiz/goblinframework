package org.goblinframework.transport.server.manager

import io.netty.channel.nio.NioEventLoopGroup
import org.goblinframework.transport.server.setting.ServerSetting
import java.net.InetSocketAddress

class TransportServerImpl(private val setting: ServerSetting) {

  val boss: NioEventLoopGroup
  val worker: NioEventLoopGroup

  init {
    val socketAddress = InetSocketAddress(setting.host(), setting.port())

    boss = NioEventLoopGroup(setting.bossThreads())
    worker = NioEventLoopGroup(setting.bossThreads())


  }

  internal fun close() {

  }
}