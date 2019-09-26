package org.goblinframework.remote.client.service

import org.goblinframework.api.remote.ImportService
import org.goblinframework.core.util.GoblinField
import org.goblinframework.core.util.ReflectionUtils
import org.goblinframework.remote.core.module.GoblinRemoteException
import org.goblinframework.remote.core.service.RemoteServiceId

class ImportServiceField
internal constructor(private val bean: Any,
                     private val field: GoblinField,
                     private val annotation: ImportService) {

  internal fun inject() {
    RemoteClientRegistryManager.INSTANCE.getRegistry() ?: return
    val serviceId = generateRemoteServiceId()
    val interceptor = RemoteClientInterceptor(serviceId)
    val proxy = ReflectionUtils.createProxy(serviceId.interfaceClass, interceptor)
    field.set(bean, proxy)
  }

  private fun generateRemoteServiceId(): RemoteServiceId {
    val interfaceClass = annotation.interfaceClass.javaObjectType
    if (!interfaceClass.isInterface) {
      throw GoblinRemoteException()
    }
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