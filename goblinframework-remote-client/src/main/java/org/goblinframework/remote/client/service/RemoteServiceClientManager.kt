package org.goblinframework.remote.client.service

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@ThreadSafe
@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.service")
class RemoteServiceClientManager private constructor()
  : GoblinManagedObject(), RemoteServiceClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServiceClientManager()
  }
}