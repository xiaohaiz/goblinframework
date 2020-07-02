package org.goblinframework.dao.mongo.bson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;

@SuppressWarnings("deprecation")
final public class BsonObjectIdDeserializer extends JsonDeserializer<ObjectId> {

  @Override
  public ObjectId deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    Object object = p.getEmbeddedObject();
    if (object instanceof de.undercouch.bson4jackson.types.ObjectId) {
      int t = ((de.undercouch.bson4jackson.types.ObjectId) object).getTime();
      int m = ((de.undercouch.bson4jackson.types.ObjectId) object).getMachine();
      int i = ((de.undercouch.bson4jackson.types.ObjectId) object).getInc();
      return ObjectId.createFromLegacyFormat(t, m, i);
    }
    if (object instanceof String && ObjectId.isValid((String) object)) {
      return new ObjectId((String) object);
    }
    return null;
  }
}
