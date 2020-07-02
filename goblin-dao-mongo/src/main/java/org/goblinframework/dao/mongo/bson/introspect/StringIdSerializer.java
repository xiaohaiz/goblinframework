package org.goblinframework.dao.mongo.bson.introspect;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;
import org.goblinframework.dao.mongo.bson.GoblinBsonGenerator;

import java.io.IOException;

/**
 * Programmatic usage only, internal usage only.
 * When serializing {@code Id} field:
 * 1. if the text is normal string, serialize it as string.
 * 2. if the text is objectId string, serialize it as {@code org.bson.types.ObjectId}.
 *
 * @author Xiaohai Zhang
 * @since Dec 5, 2019
 */
final public class StringIdSerializer extends JsonSerializer<String> {

  @Override
  public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value == null) {
      serializers.defaultSerializeNull(gen);
    } else {
      if (ObjectId.isValid(value)) {
        GoblinBsonGenerator.cast(gen).writeObjectId(new ObjectId(value));
      } else {
        gen.writeString(value);
      }
    }
  }
}
