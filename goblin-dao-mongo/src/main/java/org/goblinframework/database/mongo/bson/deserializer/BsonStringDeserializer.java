package org.goblinframework.database.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.bson.types.ObjectId;
import org.goblinframework.core.util.DateFormatUtils;

import java.io.IOException;
import java.util.Date;

final public class BsonStringDeserializer extends JsonDeserializer<String> {

  @SuppressWarnings("deprecation")
  @Override
  public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
    Object value = p.getEmbeddedObject();
    if (value == null) {
      return null;
    }
    // BsonString
    if (value instanceof String) {
      return (String) value;
    }
    // BsonBoolean
    if (value instanceof Boolean) {
      return value.toString();
    }
    // BsonInt32
    if (value instanceof Integer) {
      return value.toString();
    }
    // BsonInt64
    if (value instanceof Long) {
      return value.toString();
    }
    // BsonDouble
    if (value instanceof Double) {
      return value.toString();
    }
    // BsonObjectId
    if (value instanceof de.undercouch.bson4jackson.types.ObjectId) {
      de.undercouch.bson4jackson.types.ObjectId legacy = (de.undercouch.bson4jackson.types.ObjectId) value;
      int time = legacy.getTime();
      int machine = legacy.getMachine();
      int inc = legacy.getInc();
      ObjectId objectId = ObjectId.createFromLegacyFormat(time, machine, inc);
      return objectId.toString();
    }
    // BsonDateTime
    if (value instanceof Date) {
      return DateFormatUtils.format((Date) value);
    }
    return null;
  }
}
