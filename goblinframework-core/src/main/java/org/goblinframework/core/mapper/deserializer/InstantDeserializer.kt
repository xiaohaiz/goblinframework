package org.goblinframework.core.mapper.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import java.time.Instant

class InstantDeserializer : StdScalarDeserializer<Instant>(Instant::class.java) {

  override fun deserialize(p: JsonParser, ctx: DeserializationContext?): Instant? {
    val value = p.valueAsString ?: return null
    return try {
      Instant.ofEpochMilli(value.toLong())
    } catch (ex: NumberFormatException) {
      null
    }
  }
}