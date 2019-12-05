package org.goblinframework.management.server

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.management.IManagementServerManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.atomic.AtomicReference

@Singleton
@GoblinManagedBean(type = "management")
class ManagementServerManager private constructor()
  : GoblinManagedObject(), IManagementServerManager, ManagementServerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = ManagementServerManager()
  }

  private val running = AtomicReference<ManagementServer>()

  @Synchronized
  override fun start() {
    if (running.get() == null) {
      val ms = ManagementServer()
      ms.initialize()
      running.set(ms)
    }
  }

  override fun stop() {
    running.getAndSet(null)?.dispose()
  }

  override fun isRunning(): Boolean {
    return running.get() != null
  }

  @Install
  class Installer : IManagementServerManager by INSTANCE
}