package org.goblinframework.dao.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.goblinframework.core.util.DateUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

final public class BsonCalendarDeserializer extends JsonDeserializer<Calendar> {

  @Override
  public Calendar deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    Object value = p.getEmbeddedObject();
    Date date = DateUtils.parseDate(value);
    if (date != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar;
    }
    return null;
  }
}
