package org.goblinframework.dao.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * 1. BsonInt64 -> Long
 * 2. BsonInt32 -> Long
 * 3. BsonDouble -> Long, may lost precision
 * 4. BsonString -> Long, if string is parsable long
 * 5. BsonDateTime -> Long, Date.getTime
 *
 * @author Xiaohai Zhang
 * @since Dec 5, 2019
 */
final public class BsonLongDeserializer extends JsonDeserializer<Long> {

  @Override
  public Long deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    Object value = p.getEmbeddedObject();
    if (value instanceof Long) {
      // BsonInt64
      return (Long) value;
    }
    if (value instanceof Integer) {
      // BsonInt32
      Integer i = (Integer) value;
      return i.longValue();
    }
    if (value instanceof Double) {
      // BsonDouble
      Double d = (Double) value;
      return d.longValue();
    }
    if (value instanceof String) {
      // BsonString
      String s = (String) value;
      try {
        return Long.parseLong(s);
      } catch (NumberFormatException ex) {
        return null;
      }
    }
    if (value instanceof Date) {
      // BsonDateTime
      return ((Date) value).getTime();
    }
    return null;
  }
}
