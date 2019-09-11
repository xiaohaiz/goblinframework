package org.goblinframework.transport.core.protocol.encoder

import io.netty.buffer.ByteBufOutputStream
import org.goblinframework.core.util.JsonUtils
import org.goblinframework.transport.core.protocol.HandshakeResponse

object HandshakeResponseEncoder {

  fun encode(out: ByteBufOutputStream, response: HandshakeResponse) {
    out.writeByte(0)
    val m = mutableMapOf<String, Any>()
    m["id"] = "HandshakeResponse"
    m["success"] = response.success
    response.extensions?.run {
      m["extensions"] = this
    }
    val bs = JsonUtils.getDefaultObjectMapper().writeValueAsBytes(m)
    out.write(bs)
  }

}
