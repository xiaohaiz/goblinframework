package org.goblinframework.core.system

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.common.Ordered
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.api.system.IGoblinSystemManager
import java.util.concurrent.atomic.AtomicReference

@Singleton
@GoblinManagedBean(type = "core")
class GoblinSystemManager private constructor()
  : GoblinManagedObject(), IGoblinSystemManager, GoblinSystemManagerMXBean {

  companion object {
    @JvmField val INSTANCE = GoblinSystemManager()
  }

  private val running = AtomicReference<GoblinSystemImpl>()

  @Synchronized
  override fun start() {
    if (running.get() == null) {
      running.set(GoblinSystemImpl())
    }
  }

  override fun stop() {
    running.getAndSet(null)?.dispose()
    dispose()
  }

  override fun isRunning(): Boolean {
    return running.get() != null
  }

  @Install
  class Installer : Ordered, IGoblinSystemManager by INSTANCE {
    override fun getOrder(): Int {
      return Ordered.HIGHEST_PRECEDENCE
    }
  }
}