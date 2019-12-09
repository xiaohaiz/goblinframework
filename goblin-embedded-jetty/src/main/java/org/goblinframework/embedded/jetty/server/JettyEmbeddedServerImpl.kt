package org.goblinframework.embedded.jetty.server

import org.eclipse.jetty.server.GoblinHttpConnectionFactory
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.gzip.GzipHandler
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.goblinframework.api.function.Disposable
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.embedded.core.setting.ServerSetting
import java.util.concurrent.TimeUnit

class JettyEmbeddedServerImpl(private val setting: ServerSetting) : Disposable {

  private val server: Server

  init {
    val maxThreads = setting.threadPoolSetting().maximumPoolSize()
    val minThreads = setting.threadPoolSetting().corePoolSize()
    val unit = setting.threadPoolSetting().unit()
    val time = setting.threadPoolSetting().keepAliveTime()
    val idleTimeout = TimeUnit.MILLISECONDS.convert(time, unit)
    val threadPool = QueuedThreadPool(maxThreads, minThreads, idleTimeout.toInt())
    server = Server(threadPool)
    val connector = ServerConnector(server)
    connector.connectionFactories = listOf(GoblinHttpConnectionFactory())
    connector.host = setting.networkSetting().host()
    connector.port = setting.networkSetting().port()
    server.addConnector(connector)

    val handlers = ContextHandlerCollection()
    setting.handlerSettings().values.forEach {
      val contextHandler = ContextHandler()
      contextHandler.contextPath = it.contextPath()
      contextHandler.resourceBase = "."
      contextHandler.classLoader = ClassUtils.getDefaultClassLoader()
      contextHandler.isCompactPath = true
      contextHandler.handler = JettyHttpRequestHandler(it.servletHandler())
      val gzipHandler = GzipHandler()
      gzipHandler.handler = contextHandler
      handlers.addHandler(gzipHandler)
    }
    server.handler = handlers
    server.start()

  }

  override fun dispose() {
    server.stop()
  }
}