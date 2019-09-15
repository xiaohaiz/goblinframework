package org.goblinframework.core.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdSerializer extends StdScalarSerializer<ObjectId> {

  ObjectIdSerializer() {
    super(ObjectId.class);
  }

  @Override
  public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeString(value.toHexString());
  }
}
