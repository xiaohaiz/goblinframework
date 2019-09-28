package org.goblinframework.database.mongo.bson.introspect

import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import org.goblinframework.api.annotation.Ignore

class BsonIntrospector : JacksonAnnotationIntrospector() {

  override fun _isIgnorable(a: Annotated?): Boolean {
    return _findAnnotation(a, Ignore::class.java)?.run { true }
        ?: super._isIgnorable(a)
  }

}