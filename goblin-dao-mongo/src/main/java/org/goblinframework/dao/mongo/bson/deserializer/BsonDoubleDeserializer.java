package org.goblinframework.dao.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 1. BsonDouble -> Double
 * 2. BsonInt32 -> Double
 * 3. BsonInt64 -> Double
 * 4. BsonString -> Double, if string is parsable double
 * 5. Otherwise returns null
 *
 * @author Xiaohai Zhang
 * @since Dec 5, 2019
 */
final public class BsonDoubleDeserializer extends JsonDeserializer<Double> {

  @Override
  public Double deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    Object value = p.getEmbeddedObject();
    if (value instanceof Double) {
      // BsonDouble
      return (Double) value;
    }
    if (value instanceof Integer) {
      // BsonInt32
      Integer i = (Integer) value;
      return i.doubleValue();
    }
    if (value instanceof Long) {
      // BsonInt64
      Long l = (Long) value;
      return l.doubleValue();
    }
    if (value instanceof String) {
      // BsonString
      String s = (String) value;
      try {
        return Double.parseDouble(s);
      } catch (NumberFormatException ex) {
        return null;
      }
    }
    return null;
  }
}
