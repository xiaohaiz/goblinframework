package org.goblinframework.core.system

import org.goblinframework.api.common.Install
import org.goblinframework.api.common.Singleton
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.api.system.GoblinSystemException
import org.goblinframework.api.system.IGoblinSystemManager
import org.goblinframework.api.system.RuntimeMode
import org.goblinframework.core.util.RandomUtils
import java.util.concurrent.atomic.AtomicReference

@Singleton
@GoblinManagedBean(type = "core")
class GoblinSystemManager private constructor()
  : GoblinManagedObject(), IGoblinSystemManager, GoblinSystemManagerMXBean {

  companion object {
    @JvmField val INSTANCE = GoblinSystemManager()
  }

  private val applicationId = RandomUtils.nextObjectId()
  private val running = AtomicReference<GoblinSystemImpl>()

  @Synchronized
  override fun start() {
    if (running.get() == null) {
      val gs = GoblinSystemImpl()
      gs.initialize()
      running.set(gs)
    }
  }

  override fun stop() {
    running.getAndSet(null)?.dispose()
    dispose()
  }

  override fun isRunning(): Boolean {
    return running.get() != null
  }

  override fun applicationId(): String {
    return applicationId
  }

  override fun applicationName(): String {
    return running.get()?.applicationName() ?: throw GoblinSystemException("GOBLIN system not started")
  }

  override fun runtimeMode(): RuntimeMode {
    return running.get()?.runtimeMode() ?: throw GoblinSystemException("GOBLIN system not started")
  }

  @Install
  class Installer : IGoblinSystemManager by INSTANCE
}