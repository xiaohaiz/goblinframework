package org.goblinframework.dao.mongo.bson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;
import org.goblinframework.database.mongo.bson.GoblinBsonGenerator;

import java.io.IOException;

final public class BsonObjectIdSerializer extends JsonSerializer<ObjectId> {

  @Override
  public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value == null) {
      serializers.defaultSerializeNull(gen);
      return;
    }
    if (!(gen instanceof GoblinBsonGenerator)) {
      throw new UnsupportedOperationException();
    }
    ((GoblinBsonGenerator) gen).writeObjectId(value);
  }
}
