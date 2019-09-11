package org.goblinframework.transport.server.manager

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject

@GoblinManagedBean("TRANSPORT.SERVER")
class TransportServerManager private constructor() : GoblinManagedObject(), TransportServerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = TransportServerManager()
  }

  fun close() {
    unregisterIfNecessary()
  }
}