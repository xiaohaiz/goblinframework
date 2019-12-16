package org.goblinframework.remote.server.service

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.util.HostAndPort
import org.goblinframework.rpc.service.RemoteServiceId

class RemoteServiceManagedImpl
internal constructor(serviceId: RemoteServiceId,
                     serverAddresses: List<HostAndPort>,
                     private val serviceBean: ContainerManagedBean)
  : RemoteService(serviceId, serverAddresses) {

  override fun getServiceType(): Class<*> {
    return serviceBean.type!!
  }

  override fun getServiceBean(): Any {
    return serviceBean.getBean()!!
  }
}