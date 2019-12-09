package org.goblinframework.dao.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.goblinframework.core.util.DateUtils;

import java.io.IOException;
import java.util.Date;

final public class BsonDateDeserializer extends JsonDeserializer<Date> {

  @Override
  public Date deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    Object value = p.getEmbeddedObject();
    return DateUtils.parseDate(value);
  }
}
