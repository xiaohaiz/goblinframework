package org.goblinframework.embedded.core.manager

import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.api.core.ServiceInstaller
import org.goblinframework.embedded.core.EmbeddedServer
import org.goblinframework.embedded.core.EmbeddedServerMode
import org.goblinframework.embedded.core.module.spi.EmbeddedServerFactory
import org.goblinframework.embedded.core.setting.ServerSetting
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean(type = "embedded")
class EmbeddedServerManager private constructor()
  : GoblinManagedObject(), EmbeddedServerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = EmbeddedServerManager()
  }

  private val factories = EnumMap<EmbeddedServerMode, EmbeddedServerFactory>(EmbeddedServerMode::class.java)
  private val lock = ReentrantReadWriteLock()
  private val servers = mutableMapOf<String, DelegatedEmbeddedServer>()

  init {
    ServiceInstaller.asList(EmbeddedServerFactory::class.java).forEach {
      val mode = it.mode()
      factories[mode]?.run {
        throw UnsupportedOperationException("Duplicated EmbeddedServerFactory not allowed")
      }
      factories[mode] = it
    }
  }

  fun createServer(setting: ServerSetting): EmbeddedServer {
    val name = setting.name()
    val mode = setting.mode()
    val factory = factories[mode]
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