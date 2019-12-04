package org.goblinframework.transport.client.handler

import org.goblinframework.transport.client.channel.TransportClient

@FunctionalInterface
interface TransportClientConnectedHandler {

  fun handleTransportClientConnected(client: TransportClient)

}