package org.goblinframework.core.mapper.deserializer;

import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import org.bson.types.ObjectId;

public class Deserializers extends SimpleDeserializers {
  private static final long serialVersionUID = -2336261425139588547L;

  public Deserializers() {
    addDeserializer(ObjectId.class, new ObjectIdDeserializer());
  }
}
