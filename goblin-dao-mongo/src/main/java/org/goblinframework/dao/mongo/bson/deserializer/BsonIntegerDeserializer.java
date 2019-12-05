package org.goblinframework.dao.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 1. BsonInt32 -> Integer
 * 2. BsonInt64 -> Integer, may lost precision
 * 3. BsonDouble -> Integer, may lost precision
 * 4. BsonString -> Integer, if string is parsable integer
 * 5. Otherwise returns null
 *
 * @author Xiaohai Zhang
 * @since Dec 5, 2019
 */
final public class BsonIntegerDeserializer extends JsonDeserializer<Integer> {

  @Override
  public Integer deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    Object value = p.getEmbeddedObject();
    if (value instanceof Integer) {
      // BsonInt32
      return (Integer) value;
    }
    if (value instanceof Long) {
      // BsonInt64
      Long l = (Long) value;
      return l.intValue();
    }
    if (value instanceof Double) {
      // BsonDouble
      Double d = (Double) value;
      return d.intValue();
    }
    if (value instanceof String) {
      // BsonString
      try {
        return Integer.parseInt((String) value);
      } catch (NumberFormatException ex) {
        return null;
      }
    }
    return null;
  }
}
