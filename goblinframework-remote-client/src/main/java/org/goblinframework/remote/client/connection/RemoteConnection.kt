package org.goblinframework.remote.client.connection

import org.goblinframework.api.common.ReferenceCount
import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.transport.client.channel.TransportClient
import org.goblinframework.transport.client.channel.TransportClientManager
import org.goblinframework.transport.client.setting.TransportClientSetting
import java.util.concurrent.atomic.AtomicInteger

@GoblinManagedBean(type = "RemoteClient")
class RemoteConnection
internal constructor(private val connectionId: RemoteConnectionId)
  : GoblinManagedObject(), ReferenceCount, RemoteConnectionMXBean {

  private val referenceCount = AtomicInteger()
  val transportClient: TransportClient

  init {
    val setting = TransportClientSetting.builder()
        .name(connectionId.name())
        .serverId(connectionId.serverId)
        .serverHost(connectionId.serverHost)
        .serverPort(connectionId.serverPort)
        .autoReconnect(true)
        .enableDebugMode()
        .applyHandlerSetting { it.enableMessageFlight() }
        .build()
    val clientManager = TransportClientManager.INSTANCE
    transportClient = clientManager.createConnection(setting)
  }

  fun id(): RemoteConnectionId {
    return connectionId
  }

  override fun count(): Int {
    return referenceCount.get()
  }

  override fun retain() {
    referenceCount.incrementAndGet()
  }

  override fun retain(increment: Int) {
    throw UnsupportedOperationException()
  }

  override fun release(): Boolean {
    return referenceCount.decrementAndGet() <= 0
  }

  override fun release(decrement: Int): Boolean {
    throw UnsupportedOperationException()
  }

  override fun disposeBean() {
    val clientManager = TransportClientManager.INSTANCE
    clientManager.closeConnection(connectionId.name())
  }

  override fun getName(): String {
    return connectionId.name()
  }
}