package org.goblinframework.dao.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.goblinframework.core.util.DateUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

final public class BsonInstantDeserializer extends JsonDeserializer<Instant> {

  @Override
  public Instant deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    Object value = p.getEmbeddedObject();
    Date date = DateUtils.parseDate(value);
    if (date != null) {
      return date.toInstant();
    }
    return null;
  }
}
