package org.goblinframework.core.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdDeserializer extends StdScalarDeserializer<ObjectId> {

  public ObjectIdDeserializer() {
    super(ObjectId.class);
  }

  @Override
  public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    String value = p.getValueAsString();
    if (!ObjectId.isValid(value)) {
      return null;
    }
    return new ObjectId(value);
  }
}
