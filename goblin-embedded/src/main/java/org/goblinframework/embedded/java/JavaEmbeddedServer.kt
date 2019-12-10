package org.goblinframework.embedded.java

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerId
import org.goblinframework.embedded.server.EmbeddedServerMXBean
import org.goblinframework.embedded.server.EmbeddedServerMode
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("Embedded")
class JavaEmbeddedServer internal constructor(private val setting: ServerSetting)
  : GoblinManagedObject(), EmbeddedServer, EmbeddedServerMXBean {

  private val server = AtomicReference<JdkEmbeddedServerImpl?>()

  override fun id(): EmbeddedServerId {
    return EmbeddedServerId(EmbeddedServerMode.JAVA, setting.name())
  }

  @Synchronized
  override fun start() {
    if (server.get() != null) {
      return
    }
    server.set(JdkEmbeddedServerImpl(setting))
    logger.debug("{EMBEDDED} Embedded server [{}] started at [{}:{}]",
        id().asText(), server.get()!!.host, server.get()!!.port)
  }

  @Synchronized
  override fun stop() {
    server.getAndSet(null)?.run {
      this.stop()
      logger.debug("{EMBEDDED} Embedded server [{}] stopped", id().asText())
    }
  }

  @Synchronized
  override fun isRunning(): Boolean {
    return server.get() != null
  }

}