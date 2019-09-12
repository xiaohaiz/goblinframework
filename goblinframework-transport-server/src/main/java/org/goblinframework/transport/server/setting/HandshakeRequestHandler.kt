package org.goblinframework.transport.server.setting

import org.goblinframework.transport.core.protocol.HandshakeRequest

interface HandshakeRequestHandler {

  fun handleHandshakeRequest(request: HandshakeRequest): Boolean

}
