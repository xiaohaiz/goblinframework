package org.goblinframework.remote.server.service

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@ThreadSafe
@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.service")
class RemoteServiceManager private constructor()
  : GoblinManagedObject(), RemoteServiceManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServiceManager()
  }

  fun unregisterAll() {}
}