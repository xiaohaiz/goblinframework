package org.goblinframework.transport.server.handler

import org.goblinframework.transport.core.protocol.HandshakeRequest

interface HandshakeRequestHandler {

  fun handleHandshakeRequest(request: HandshakeRequest): Boolean

}
