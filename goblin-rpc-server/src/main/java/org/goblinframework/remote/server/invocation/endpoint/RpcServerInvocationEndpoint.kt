package org.goblinframework.remote.server.invocation.endpoint

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.concurrent.GoblinFuture
import org.goblinframework.api.function.ValueWrapper
import org.goblinframework.api.reactor.GoblinPublisher
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.server.invocation.RemoteServerEndpointMXBean
import org.goblinframework.remote.server.invocation.RemoteServerInvocation
import org.goblinframework.remote.server.invocation.RpcServerEndpoint
import org.goblinframework.rpc.filter.RpcFilterChain
import org.goblinframework.rpc.protocol.RpcResponseCode
import java.util.concurrent.atomic.LongAdder

@Singleton
@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.invocation")
class RpcServerInvocationEndpoint private constructor()
  : GoblinManagedObject(), RpcServerEndpoint, RemoteServerEndpointMXBean {

  companion object {
    @JvmField val INSTANCE = RpcServerInvocationEndpoint()
  }

  private val executionCount = LongAdder()
  private val successCount = LongAdder()
  private val failureCount = LongAdder()

  override fun filter(invocation: RemoteServerInvocation, chain: RpcFilterChain<RemoteServerInvocation>) {
    executionCount.increment()
    try {
      invocation.response.executionTime = System.currentTimeMillis()
      var result = invocation.service.invoke(invocation.method, invocation.arguments)
      if (result is GoblinFuture<*>) {
        result = if (result is ValueWrapper<*>) {
          (result as ValueWrapper<*>).value
        } else {
          result.get()
        }
      } else if (result is GoblinPublisher<*>) {
        result = result.block()
      }
      // 如果請求是jsonMode那麽就把結果轉換成json字符串
      if (invocation.request.jsonMode) {
        if (result != null) {
          invocation.response.result = JsonMapper.toJson(result)
        }
      } else {
        invocation.response.result = result
      }
      successCount.increment()
    } catch (ex: Throwable) {
      failureCount.increment()
      logger.error("{SERVER_SERVICE_EXECUTION_ERROR} " +
          "Exception raised when executing service method [{}]",
          invocation.asText(), ex)
      invocation.writeError(RpcResponseCode.SERVER_SERVICE_EXECUTION_ERROR, ex)
    } finally {
      val current = System.currentTimeMillis()
      invocation.response.executionDuration = (current - invocation.response.executionTime)
    }
  }

  override fun getName(): String {
    return "RemoteServerInvocationEndpoint"
  }

  override fun getExecutionCount(): Long {
    return executionCount.sum()
  }

  override fun getSuccessCount(): Long {
    return successCount.sum()
  }

  override fun getFailureCount(): Long {
    return failureCount.sum()
  }
}