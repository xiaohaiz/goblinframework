package org.goblinframework.embedded.core.manager

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.ServiceInstaller
import org.goblinframework.embedded.core.EmbeddedServer
import org.goblinframework.embedded.core.EmbeddedServerFactory
import org.goblinframework.embedded.core.EmbeddedServerMode
import org.goblinframework.embedded.core.setting.ServerSetting
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean("EMBEDDED")
class EmbeddedServerManager private constructor()
  : GoblinManagedObject(), EmbeddedServerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = EmbeddedServerManager()
  }

  private val factories = EnumMap<EmbeddedServerMode, EmbeddedServerFactory>(EmbeddedServerMode::class.java)
  private val lock = ReentrantReadWriteLock()
  private val servers = mutableMapOf<String, DelegatedEmbeddedServer>()

  init {
    ServiceInstaller.installedList(EmbeddedServerFactory::class.java).forEach {
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
      this.unregisterIfNecessary()
    }
  }

  fun close() {
    unregisterIfNecessary()
    lock.write {
      servers.values.forEach {
        it.stop()
        it.unregisterIfNecessary()
      }
      servers.clear()
    }
  }
}