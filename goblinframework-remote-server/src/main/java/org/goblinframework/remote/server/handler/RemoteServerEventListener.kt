package org.goblinframework.remote.server.handler

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.reflection.ReflectionUtils
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.remote.core.protocol.RemoteRequest
import org.goblinframework.remote.core.protocol.RemoteResponse
import org.goblinframework.remote.server.expose.ExposeServiceId
import org.goblinframework.remote.server.service.RemoteServiceManager

@Install
@GoblinEventChannel("/goblin/remote/server")
class RemoteServerEventListener : GoblinEventListener {

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is RemoteServerEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as RemoteServerEvent
    val request = event.ctx.requestReader.readPayload()!! as RemoteRequest

    val interfaceClass = ClassUtils.loadClass(request.serviceInterface)
    val id = ExposeServiceId(interfaceClass, request.serviceGroup, request.serviceVersion)
    RemoteServiceManager.INSTANCE.remoteService(id)?.run {
      val parameterTypes = Array(request.parameterTypes.size) {
        ClassUtils.loadClass(request.parameterTypes[it])
      }
      val method = this.type().getMethod(request.methodName, *parameterTypes)
      val bean = this.bean()
      val result = ReflectionUtils.invoke(bean, method, request.arguments)

      val response = RemoteResponse()
      response.result = result
      event.ctx.responseWriter.writePayload(response)
      event.ctx.sendResponse()
    }
  }
}