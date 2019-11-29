package org.goblinframework.remote.server.service

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.remote.core.service.RemoteServiceId

class RemoteServiceManagedImpl
internal constructor(serviceId: RemoteServiceId,
                     private val serviceBean: ContainerManagedBean)
  : RemoteService(serviceId) {

  override fun getServiceType(): Class<*> {
    return serviceBean.type!!
  }

  override fun getServiceBean(): Any {
    return serviceBean.getBean()!!
  }
}