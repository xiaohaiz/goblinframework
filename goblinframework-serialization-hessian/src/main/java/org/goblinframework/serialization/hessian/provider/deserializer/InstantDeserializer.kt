package org.goblinframework.serialization.hessian.provider.deserializer

import com.caucho.hessian.io.AbstractStringValueDeserializer
import java.time.Instant

class InstantDeserializer : AbstractStringValueDeserializer() {

  override fun getType(): Class<*> {
    return Instant::class.java
  }

  override fun create(value: String): Any {
    val epochMilli = value.toLong()
    return Instant.ofEpochMilli(epochMilli)
  }
}
