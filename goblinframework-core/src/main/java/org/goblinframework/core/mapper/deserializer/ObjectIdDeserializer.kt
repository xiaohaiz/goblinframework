package org.goblinframework.core.mapper.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import org.bson.types.ObjectId

class ObjectIdDeserializer : StdScalarDeserializer<ObjectId>(ObjectId::class.java) {

  override fun deserialize(p: JsonParser, ctx: DeserializationContext): ObjectId? {
    val value = p.valueAsString
    return if (!ObjectId.isValid(value)) null else ObjectId(value)
  }
}
