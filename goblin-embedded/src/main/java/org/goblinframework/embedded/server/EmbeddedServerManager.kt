package org.goblinframework.embedded.server

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.function.Disposable
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.embedded.setting.ServerSetting
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean("Embedded")
class EmbeddedServerManager private constructor() : GoblinManagedObject(), EmbeddedServerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = EmbeddedServerManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val servers = mutableMapOf<EmbeddedServerId, EmbeddedServer>()

  fun createServer(setting: ServerSetting): EmbeddedServer {
    val name = setting.name()
    val mode = setting.mode()
    val factory = EmbeddedServerFactoryManager.INSTANCE.getFactory(mode)
        ?: throw UnsupportedOperationException("EmbeddedServerMode not available: $mode")
    val id = EmbeddedServerId(mode, name)
    return lock.write {
      servers[id]?.run {
        throw GoblinDuplicateException("EmbeddedServer [$mode/$name] already exists")
      }
      val server = factory.createEmbeddedServer(setting)
      servers[id] = server
      server
    }
  }

  fun closeServer(id: EmbeddedServerId) {
    lock.write { servers.remove(id) }?.run {
      this.stop()
      (this as? Disposable)?.dispose()
    }
  }

  override fun getEmbeddedServerList(): Array<EmbeddedServerMXBean> {
    return lock.read {
      servers.values.filterIsInstance(EmbeddedServerMXBean::class.java).toTypedArray()
    }
  }

  override fun disposeBean() {
    lock.write {
      servers.values.forEach {
        it.stop()
        (this as? Disposable)?.dispose()
      }
      servers.clear()
    }
  }
}