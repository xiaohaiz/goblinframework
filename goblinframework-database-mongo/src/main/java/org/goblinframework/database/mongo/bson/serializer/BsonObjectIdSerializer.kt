package org.goblinframework.database.mongo.bson.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bson.types.ObjectId
import org.goblinframework.database.mongo.bson.GoblinBsonGenerator

class BsonObjectIdSerializer : JsonSerializer<ObjectId>() {

  override fun serialize(value: ObjectId?, gen: JsonGenerator, serializers: SerializerProvider) {
    value?.run {
      trySerialize(this, gen)
    } ?: kotlin.run {
      serializers.defaultSerializeNull(gen)
    }
  }

  private fun trySerialize(value: ObjectId, gen: JsonGenerator) {
    (gen as? GoblinBsonGenerator)?.run {
      this.writeObjectId(value)
    } ?: throw UnsupportedOperationException()
  }
}
