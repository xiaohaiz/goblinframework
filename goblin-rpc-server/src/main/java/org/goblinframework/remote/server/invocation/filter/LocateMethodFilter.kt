package org.goblinframework.remote.server.invocation.filter

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.util.ReflectionUtils
import org.goblinframework.remote.server.invocation.RemoteServerInvocation
import org.goblinframework.rpc.filter.RpcFilterChain
import org.goblinframework.rpc.protocol.RpcResponseCode

@Singleton
class LocateMethodFilter private constructor() : AbstractInternalFilter() {

  companion object {
    @JvmField val INSTANCE = LocateMethodFilter()
  }

  override fun doFilter(invocation: RemoteServerInvocation, chain: RpcFilterChain<RemoteServerInvocation>) {
    val request = invocation.request
    val interfaceClass = invocation.interfaceClass
    val methodName = request.methodName
    val parameterTypes = request.parameterTypes
    try {
      invocation.method = ReflectionUtils.getMethod(interfaceClass, methodName, parameterTypes)
    } catch (ex: Exception) {
      logger.error("{SERVER_METHOD_NOT_FOUND_ERROR} " +
          "Service method not found [{}]",
          invocation.asText(), ex)
      invocation.writeError(RpcResponseCode.SERVER_METHOD_NOT_FOUND_ERROR, ex)
      return
    }

    if (request.returnType != null) {
      val actualReturnType = invocation.method.returnType.name
      if (request.returnType != actualReturnType) {
        invocation.method = null
        logger.error("{SERVER_METHOD_NOT_FOUND_ERROR} " +
            "Service method return type mismatch, expect {} but actual {} [{}]",
            request.returnType, actualReturnType, invocation.asText())
        invocation.writeError(RpcResponseCode.SERVER_METHOD_NOT_FOUND_ERROR)
        return
      }
    }

    chain.filter(invocation)
  }
}