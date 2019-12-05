package org.goblinframework.dao.mongo.bson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.goblinframework.dao.mongo.bson.GoblinBsonGenerator;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

final public class BsonInstantSerializer extends JsonSerializer<Instant> {

  @Override
  public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value == null) {
      serializers.defaultSerializeNull(gen);
    } else {
      GoblinBsonGenerator.cast(gen).writeDateTime(Date.from(value));
    }
  }
}
