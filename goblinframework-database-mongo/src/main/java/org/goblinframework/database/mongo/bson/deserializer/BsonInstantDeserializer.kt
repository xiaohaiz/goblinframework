package org.goblinframework.database.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.goblinframework.core.util.DateFormatUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class BsonInstantDeserializer extends JsonDeserializer<Instant> {

  @Override
  public Instant deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    Date date = parseDate(p.getEmbeddedObject());
    if (date == null) {
      return null;
    }
    return date.toInstant();
  }

  private Date parseDate(Object object) {
    if (object instanceof Date) {
      return (Date) object;
    }
    if (object instanceof Integer || object instanceof Long || object instanceof Double) {
      Number number = (Number) object;
      return new Date(number.longValue());
    }
    if (object instanceof String) {
      return DateFormatUtils.parse((String) object);
    }
    return null;
  }
}
