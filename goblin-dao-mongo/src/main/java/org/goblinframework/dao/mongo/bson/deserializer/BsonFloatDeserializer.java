package org.goblinframework.dao.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 1. BsonDouble -> Float, may lost precision
 * 2. BsonInt32 -> Float
 * 3. BsonInt64 -> Float, may lost precision
 * 4. BsonString -> Float, if string is parsable float
 * 5. Otherwise returns null
 *
 * @author Xiaohai Zhang
 * @since Dec 5, 2019
 */
final public class BsonFloatDeserializer extends JsonDeserializer<Float> {

  @Override
  public Float deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    Object value = p.getEmbeddedObject();
    if (value instanceof Double) {
      // BsonDouble
      Double d = (Double) value;
      return d.floatValue();
    }
    if (value instanceof Integer) {
      // BsonInt32
      Integer i = (Integer) value;
      return i.floatValue();
    }
    if (value instanceof Long) {
      // BsonInt64
      Long l = (Long) value;
      return l.floatValue();
    }
    if (value instanceof String) {
      // BsonString
      String s = (String) value;
      try {
        return Float.parseFloat(s);
      } catch (NumberFormatException ex) {
        return null;
      }
    }
    return null;
  }
}
