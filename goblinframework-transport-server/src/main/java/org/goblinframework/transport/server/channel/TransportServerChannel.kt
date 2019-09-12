package org.goblinframework.transport.server.channel

import io.netty.channel.Channel
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.transport.core.protocol.HandshakeRequest
import org.goblinframework.transport.server.manager.TransportServerImpl
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("TRANSPORT.SERVER")
class TransportServerChannel
internal constructor(private val server: TransportServerImpl,
                     private val channel: Channel)
  : GoblinManagedObject(), TransportServerChannelMXBean {

  private val handshake = AtomicReference<HandshakeRequest>()

  fun onMessage(msg: Any) {

  }

  internal fun terminate() {
  }

  internal fun close() {
    unregisterIfNecessary()
    handshake.set(null)
  }
}