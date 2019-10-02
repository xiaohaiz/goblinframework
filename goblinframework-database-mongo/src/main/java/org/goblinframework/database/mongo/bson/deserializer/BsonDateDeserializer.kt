package org.goblinframework.database.mongo.bson.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.goblinframework.core.util.DateFormatUtils
import java.util.*

class BsonDateDeserializer : JsonDeserializer<Date>() {

  override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Date? {
    return parseDate(p.embeddedObject)
  }

  companion object {
    fun parseDate(`object`: Any?): Date? {
      if (`object` is Date) {
        return `object`
      }
      if (`object` is Int || `object` is Long || `object` is Double) {
        val number = `object` as Number
        return Date(number.toLong())
      }
      return if (`object` is String) {
        DateFormatUtils.parse(`object`)
      } else null
    }
  }
}
