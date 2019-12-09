package org.goblinframework.embedded.core.manager

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerFactoryManager
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean(type = "embedded")
class EmbeddedServerManager private constructor()
  : GoblinManagedObject(), EmbeddedServerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = EmbeddedServerManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val servers = mutableMapOf<String, DelegatedEmbeddedServer>()

  fun createServer(setting: ServerSetting): EmbeddedServer {
    val name = setting.name()
    val mode = setting.mode()
    val factory = EmbeddedServerFactoryManager.INSTANCE.getFactory(mode)
        ?: throw UnsupportedOperationException("EmbeddedServerMode not available: $mode")
    return lock.write {
      servers[name]?.run {
        throw IllegalStateException("EmbeddedServer [$name] already exists")
      }
      val server = factory.createEmbeddedServer(setting)
      servers[name] = DelegatedEmbeddedServer(server)
      server
    }
  }

  fun getServer(name: String): EmbeddedServer? {
    return lock.read { servers[name] }
  }

  fun closeServer(name: String) {
    lock.write { servers.remove(name) }?.run {
      this.stop()
      this.dispose()
    }
  }

  override fun disposeBean() {
    lock.write {
      servers.values.forEach {
        it.stop()
        it.dispose()
      }
      servers.clear()
    }
  }
}