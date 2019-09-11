package org.goblinframework.transport.core.protocol.encoder

import io.netty.buffer.ByteBufOutputStream
import org.goblinframework.core.util.JsonUtils
import org.goblinframework.transport.core.protocol.HandshakeRequest

object HandshakeRequestEncoder {

  fun encode(out: ByteBufOutputStream, request: HandshakeRequest) {
    out.writeByte(0)
    val m = mutableMapOf<String, Any>()
    m["id"] = "HandshakeRequest"
    request.serverId?.run {
      m["serverId"] = this
    }
    request.clientId?.run {
      m["clientId"] = this
    }
    request.extensions?.run {
      m["extensions"] = this
    }
    val bs = JsonUtils.getDefaultObjectMapper().writeValueAsBytes(m)
    out.write(bs)
  }

}
