package org.goblinframework.remote.server.service

import org.goblinframework.api.function.Disposable
import org.goblinframework.core.util.HostAndPort
import org.goblinframework.rpc.service.RemoteServiceId

class RemoteServiceStaticImpl
internal constructor(serviceId: RemoteServiceId,
                     serverAddresses: List<HostAndPort>,
                     private val serviceBean: Any)
  : RemoteService(serviceId, serverAddresses) {

  override fun getServiceType(): Class<*> {
    return serviceBean.javaClass
  }

  override fun getServiceBean(): Any {
    return serviceBean
  }

  override fun dispose() {
    (serviceBean as? Disposable)?.dispose()
  }
}