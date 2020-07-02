package org.goblinframework.core.mapper.introspect;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.goblinframework.api.annotation.Ignore;
import org.goblinframework.api.dao.Field;
import org.goblinframework.core.util.StringUtils;

public class JsonIntrospector extends JacksonAnnotationIntrospector {

  @Override
  public PropertyName findNameForSerialization(Annotated a) {
    Field field = _findAnnotation(a, Field.class);
    if (field != null && StringUtils.isNotBlank(field.value())) {
      return new PropertyName(field.value().trim());
    }
    return super.findNameForSerialization(a);
  }

  @Override
  public PropertyName findNameForDeserialization(Annotated a) {
    Field field = _findAnnotation(a, Field.class);
    if (field != null && StringUtils.isNotBlank(field.value())) {
      return new PropertyName(field.value().trim());
    }
    return super.findNameForDeserialization(a);
  }

  @Override
  protected boolean _isIgnorable(Annotated a) {
    Ignore annotation = _findAnnotation(a, Ignore.class);
    if (annotation != null) {
      return true;
    }
    return super._isIgnorable(a);
  }
}
