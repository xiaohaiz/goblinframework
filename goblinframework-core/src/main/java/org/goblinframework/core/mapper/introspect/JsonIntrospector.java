package org.goblinframework.core.mapper.introspect;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.goblinframework.api.core.Ignore;

public class JsonIntrospector extends JacksonAnnotationIntrospector {

  @Override
  protected boolean _isIgnorable(Annotated a) {
    Ignore annotation = _findAnnotation(a, Ignore.class);
    if (annotation != null) {
      return true;
    }
    return super._isIgnorable(a);
  }
}
