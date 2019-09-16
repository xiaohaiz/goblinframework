package org.goblinframework.core.mapper.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import java.time.Instant

class InstantSerializer : StdScalarSerializer<Instant>(Instant::class.java) {

  override fun serialize(value: Instant, gen: JsonGenerator, provider: SerializerProvider?) {
    gen.writeNumber(value.toEpochMilli())
  }
}