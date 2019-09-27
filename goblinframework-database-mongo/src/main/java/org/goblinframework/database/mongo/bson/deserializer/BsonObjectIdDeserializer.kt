package org.goblinframework.database.mongo.bson.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.bson.types.ObjectId

class BsonObjectIdDeserializer : JsonDeserializer<ObjectId>() {

  override fun deserialize(p: JsonParser, ctx: DeserializationContext?): ObjectId? {
    return when (val eo = p.embeddedObject) {
      is de.undercouch.bson4jackson.types.ObjectId -> {
        val time = eo.time
        val machine = eo.machine
        val inc = eo.inc
        ObjectId.createFromLegacyFormat(time, machine, inc)
      }
      is String ->
        if (ObjectId.isValid(eo)) ObjectId(eo) else null
      else ->
        null
    }
  }
}