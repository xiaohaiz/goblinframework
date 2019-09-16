package org.goblinframework.core.mapper.serializer;

import com.fasterxml.jackson.databind.module.SimpleSerializers;
import org.bson.types.ObjectId;

public class Serializers extends SimpleSerializers {
  private static final long serialVersionUID = 1952680180474050755L;

  public Serializers() {
    addSerializer(ObjectId.class, new ObjectIdSerializer());
  }
}
