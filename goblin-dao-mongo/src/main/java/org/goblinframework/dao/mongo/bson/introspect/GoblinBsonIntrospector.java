package org.goblinframework.dao.mongo.bson.introspect;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.goblinframework.api.annotation.Ignore;
import org.goblinframework.api.dao.GoblinId;

final public class GoblinBsonIntrospector extends JacksonAnnotationIntrospector {

  @Override
  public Object findSerializer(Annotated a) {
    if (_findAnnotation(a, GoblinId.class) != null && a.getRawType() == String.class) {
      return StringIdSerializer.class;
    }
    return super.findSerializer(a);
  }

  @Override
  public PropertyName findNameForSerialization(Annotated a) {
    if (_findAnnotation(a, GoblinId.class) != null) {
      return new PropertyName("_id");
    }
    return super.findNameForSerialization(a);
  }

  @Override
  public PropertyName findNameForDeserialization(Annotated a) {
    if (_findAnnotation(a, GoblinId.class) != null) {
      return new PropertyName("_id");
    }
    return super.findNameForDeserialization(a);
  }

  @Override
  protected boolean _isIgnorable(Annotated a) {
    if (_findAnnotation(a, Ignore.class) != null) {
      return true;
    }
    return super._isIgnorable(a);
  }
}
