package org.goblinframework.database.mongo.bson.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Instant

class BsonInstantDeserializer : JsonDeserializer<Instant>() {

  override fun deserialize(p: JsonParser?, ctx: DeserializationContext): Instant? {
    return BsonDateDeserializer.parseDate(p?.embeddedObject)?.toInstant()
  }
}
