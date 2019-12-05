package org.goblinframework.dao.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 1. BsonBoolean -> Boolean
 * 2. BsonString -> Boolean, value must be "true" or "false" (ignore case)
 * 3. Otherwise returns null
 *
 * @author Xiaohai Zhang
 * @since Dec 5, 2019
 */
final public class BsonBooleanDeserializer extends JsonDeserializer<Boolean> {

  @Override
  public Boolean deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    Object value = p.getEmbeddedObject();
    if (value instanceof Boolean) {
      // BsonBoolean
      return (Boolean) value;
    }
    if (value instanceof String) {
      // BsonString
      String s = (String) value;
      if ("true".equalsIgnoreCase(s)) {
        return true;
      }
      if ("false".equalsIgnoreCase(s)) {
        return false;
      }
    }
    return null;
  }
}
