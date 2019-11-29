package org.goblinframework.remote.server.service

import org.goblinframework.api.function.Disposable
import org.goblinframework.remote.core.service.RemoteServiceId

class RemoteServiceStaticImpl
internal constructor(serviceId: RemoteServiceId,
                     private val serviceBean: Any) : RemoteService(serviceId) {

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