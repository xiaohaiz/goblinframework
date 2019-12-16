package org.goblinframework.remote.server.invocation.filter

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.util.ClassResolver
import org.goblinframework.remote.core.protocol.RemoteResponseCode
import org.goblinframework.remote.core.service.RemoteServiceId
import org.goblinframework.remote.server.invocation.RemoteServerInvocation
import org.goblinframework.remote.server.service.RemoteServiceManager
import org.goblinframework.rpc.filter.RpcFilterChain

@Singleton
class LocateServiceFilter private constructor() : AbstractInternalFilter() {

  companion object {
    @JvmField val INSTANCE = LocateServiceFilter()
  }

  override fun doFilter(invocation: RemoteServerInvocation, chain: RpcFilterChain<RemoteServerInvocation>) {
    val request = invocation.request
    try {
      invocation.interfaceClass = ClassResolver.resolve(request.serviceInterface)
    } catch (ex: ClassNotFoundException) {
      logger.error("{SERVER_SERVICE_NOT_FOUND_ERROR} " +
          "Service interface class can't be resolved [{}]",
          invocation.asText(), ex)
      invocation.writeError(RemoteResponseCode.SERVER_SERVICE_NOT_FOUND_ERROR, ex)
      return
    }

    val serviceId = RemoteServiceId(request.serviceInterface, request.serviceVersion)
    invocation.service = RemoteServiceManager.INSTANCE.getRemoteService(serviceId)
    if (invocation.service == null) {
      logger.error("{SERVER_SERVICE_NOT_FOUND_ERROR} " +
          "Service not found [{}]",
          invocation.asText())
      invocation.writeError(RemoteResponseCode.SERVER_SERVICE_NOT_FOUND_ERROR)
      return
    }

    chain.filter(invocation)
  }
}