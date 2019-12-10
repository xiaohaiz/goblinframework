package org.goblinframework.embedded.server.internal

import com.sun.net.httpserver.HttpServer
import org.goblinframework.api.function.Disposable
import org.goblinframework.core.util.NetworkUtils
import org.goblinframework.embedded.setting.ServerSetting

import java.net.InetSocketAddress
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

internal class JavaEmbeddedServerImpl(setting: ServerSetting) : Disposable {

  val host: String
  val port: Int
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
    server.createContext("/", JavaHttpRequestHandler(setting))
    server.start()

    var h = server.address.address.hostAddress
    if (h == "0:0:0:0:0:0:0:0") {
      h = NetworkUtils.ALL_HOST
    }
    host = h
    port = server.address.port
  }

  override fun dispose() {
    server.stop(0)
    executor.shutdown()
    executor.awaitTermination(5, TimeUnit.SECONDS)
  }
}
