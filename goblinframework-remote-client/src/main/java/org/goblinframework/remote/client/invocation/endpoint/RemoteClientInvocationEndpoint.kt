package org.goblinframework.remote.client.invocation.endpoint

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.client.dispatcher.response.RemoteClientResponseDispatcher
import org.goblinframework.remote.client.invocation.RemoteClientEndpoint
import org.goblinframework.remote.client.invocation.RemoteClientEndpointMXBean
import org.goblinframework.remote.client.invocation.RemoteClientInvocation
import org.goblinframework.remote.core.filter.RemoteFilterChain
import java.util.concurrent.atomic.LongAdder

@Singleton
@GoblinManagedBean("RemoteClient")
class RemoteClientInvocationEndpoint private constructor()
  : GoblinManagedObject(), RemoteClientEndpoint, RemoteClientEndpointMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientInvocationEndpoint()
  }

  private val executionCount = LongAdder()

  override fun filter(invocation: RemoteClientInvocation, chain: RemoteFilterChain<RemoteClientInvocation>) {
    if (invocation.dispatchAll()) {
      invocation.routes.forEach {
        val route = it
        try {
          invocation.flight.sendRequest(route)
        } finally {
          route.release()
        }
      }
    } else {
      val route = invocation.routes.first()
      val flightFuture = try {
        invocation.flight.sendRequest(route)
      } finally {
        route.release()
      }
      if (invocation.noResponseWait()) {
        invocation.complete(null)
      } else {
        invocation.instruction.stop()
        flightFuture.addListener {
          try {
            it.get()
          } catch (ex: Throwable) {
            invocation.transportError = ex
          }
          RemoteClientResponseDispatcher.INSTANCE.onResponse(invocation)
        }
      }
    }
  }

  override fun getName(): String {
    return "RemoteClientInvocationEndpoint"
  }

  override fun getExecutionCount(): Long {
    return executionCount.sum()
  }
}