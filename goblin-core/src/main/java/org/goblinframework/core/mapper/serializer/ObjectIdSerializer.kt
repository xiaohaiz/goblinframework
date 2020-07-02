package org.goblinframework.core.mapper.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import org.bson.types.ObjectId

class ObjectIdSerializer : StdScalarSerializer<ObjectId>(ObjectId::class.java) {

  override fun serialize(value: ObjectId, gen: JsonGenerator, provider: SerializerProvider) {
    gen.writeString(value.toHexString())
  }
}
