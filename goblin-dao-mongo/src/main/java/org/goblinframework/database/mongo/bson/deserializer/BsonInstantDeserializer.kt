package org.goblinframework.database.mongo.bson.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.goblinframework.core.util.DateUtils
import java.time.Instant

class BsonInstantDeserializer : JsonDeserializer<Instant>() {

  override fun deserialize(p: JsonParser?, ctx: DeserializationContext): Instant? {
    return DateUtils.parseDate(p?.embeddedObject)?.toInstant()
  }
}
