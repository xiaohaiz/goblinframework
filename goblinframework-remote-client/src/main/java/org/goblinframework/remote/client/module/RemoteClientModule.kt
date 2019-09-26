package org.goblinframework.remote.client.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.GoblinSubModule
import org.goblinframework.api.system.ISubModule

@Install
class RemoteClientModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_CLIENT
  }


}