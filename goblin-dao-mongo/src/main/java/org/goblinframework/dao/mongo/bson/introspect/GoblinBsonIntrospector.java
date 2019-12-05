package org.goblinframework.dao.mongo.bson.introspect;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.goblinframework.api.dao.Id;

public class GoblinBsonIntrospector extends JacksonAnnotationIntrospector {

  @Override
  public PropertyName findNameForSerialization(Annotated a) {
    if (_findAnnotation(a, Id.class) != null) {

    }
    return super.findNameForSerialization(a);
  }

  @Override
  public PropertyName findNameForDeserialization(Annotated a) {
    return super.findNameForDeserialization(a);
  }

  @Override
  protected boolean _isIgnorable(Annotated a) {
    return super._isIgnorable(a);
  }
}
