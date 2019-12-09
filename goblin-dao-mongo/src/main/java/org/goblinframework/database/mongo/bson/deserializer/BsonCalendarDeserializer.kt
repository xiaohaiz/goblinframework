package org.goblinframework.database.mongo.bson.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.goblinframework.core.util.DateUtils
import java.util.*

class BsonCalendarDeserializer : JsonDeserializer<Calendar>() {

  override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Calendar? {
    return DateUtils.parseDate(p?.embeddedObject)?.run {
      val calendar = Calendar.getInstance()
      calendar.time = this
      calendar
    }
  }
}