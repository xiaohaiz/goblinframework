package org.goblinframework.remote.client.service

import org.goblinframework.api.remote.ImportService
import org.goblinframework.core.util.GoblinField
import org.goblinframework.remote.core.module.GoblinRemoteException
import org.goblinframework.remote.core.service.RemoteServiceId

class ImportServiceField
internal constructor(private val bean: Any,
                     private val field: GoblinField,
                     private val annotation: ImportService) {

  internal fun inject() {
    RemoteClientRegistryManager.INSTANCE.getRegistry() ?: return
    val id = generateRemoteServiceId()

  }

  private fun generateRemoteServiceId(): RemoteServiceId {
    val interfaceClass = annotation.interfaceClass.javaObjectType
    if (interfaceClass !== field.fieldTypeSetterFirst) {
      throw GoblinRemoteException()
    }
    var group = "goblin"
    if (annotation.group.enable) {
      group = annotation.group.group
    }
    var version = "1.0"
    if (annotation.version.enable) {
      version = annotation.version.version
    }
    return RemoteServiceId(interfaceClass, group, version)
  }
}