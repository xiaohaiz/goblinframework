package org.goblinframework.transport.server.manager

import org.goblinframework.api.common.Lifecycle
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.transport.server.setting.ServerSetting

@GoblinManagedBean("TRANSPORT.SERVER")
class TransportServer(private val setting: ServerSetting)
  : GoblinManagedObject(), Lifecycle, TransportServerMXBean {

  override fun start() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun stop() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun isRunning(): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}