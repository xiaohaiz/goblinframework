package org.goblinframework.registry.zookeeper

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("Registry")
class ZkClient internal constructor(setting: ZkClientSetting)
  : GoblinManagedObject(), ZkClientMXBean {

  init {

  }

  override fun getTranscoder(): ZkTranscoderMXBean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}