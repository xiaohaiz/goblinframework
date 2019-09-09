package org.goblinframework.embedded.core.provider

import com.sun.net.httpserver.HttpServer
import org.goblinframework.embedded.core.setting.ServerSetting

import java.net.InetSocketAddress
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

internal class JdkEmbeddedServerImpl(setting: ServerSetting) {

  private val host: String
  private val port: Int
  private val executor: ThreadPoolExecutor
  private val server: HttpServer

  init {
    val address = InetSocketAddress(
        setting.networkSetting().host(),
        setting.networkSetting().port())
    server = HttpServer.create(address, 0)

    executor = ThreadPoolExecutor(
        setting.threadPoolSetting().corePoolSize(),
        setting.threadPoolSetting().maximumPoolSize(),
        setting.threadPoolSetting().keepAliveTime(),
        setting.threadPoolSetting().unit(),
        LinkedBlockingQueue())
    server.executor = executor
    server.start()

    host = server.address.address.hostAddress
    port = server.address.port
  }

  fun stop() {
    server.stop(0)
    executor.shutdown()
    executor.awaitTermination(5, TimeUnit.SECONDS)
  }
}
