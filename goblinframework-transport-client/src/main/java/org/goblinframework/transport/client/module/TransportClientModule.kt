package org.goblinframework.transport.client.module

import org.goblinframework.api.core.Install
import org.goblinframework.api.system.GoblinSubModule
import org.goblinframework.api.system.ISubModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInitializeContext
import org.goblinframework.transport.client.channel.TransportClientManager
import org.goblinframework.transport.client.flight.MessageFlightManager
import org.slf4j.LoggerFactory

@Install
class TransportClientModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.TRANSPORT_CLIENT
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    TransportClientManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    TransportClientManager.INSTANCE.dispose()
    MessageFlightManager.INSTANCE.dispose()
  }

  companion object {
    private val logger = LoggerFactory.getLogger(TransportClientModule::class.java)

    private const val DEFAULT_HEARTBEAT_INTERVAL_IN_SECONDS = 10
    private const val DEFAULT_MAX_SUFFERANCE_HEARTBEAT_LOST = 5

    val heartbeatIntervalInSeconds: Long by lazy {
      val s = System.getProperty("org.goblinframework.transport.client.heartbeatIntervalInSeconds")
      if (s != null) {
        logger.debug("-Dorg.goblinframework.transport.client.heartbeatIntervalInSeconds = {}", s)
        s.toLong()
      } else {
        DEFAULT_HEARTBEAT_INTERVAL_IN_SECONDS.toLong()
      }
    }

    val maxSufferanceHeartLost: Int by lazy {
      val s = System.getProperty("org.goblinframework.transport.client.maxSufferanceHeartbeatLost")
      if (s != null) {
        logger.debug("-Dorg.goblinframework.transport.client.maxSufferanceHeartbeatLost = {}", s)
        s.toInt()
      } else {
        DEFAULT_MAX_SUFFERANCE_HEARTBEAT_LOST
      }
    }

  }
}