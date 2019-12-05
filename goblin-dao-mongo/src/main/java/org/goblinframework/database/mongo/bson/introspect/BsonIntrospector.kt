package org.goblinframework.database.mongo.bson.introspect

import com.fasterxml.jackson.databind.PropertyName
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import org.goblinframework.api.annotation.Ignore
import org.goblinframework.api.dao.Id

class BsonIntrospector : JacksonAnnotationIntrospector() {

  override fun findNameForSerialization(a: Annotated): PropertyName? {
    return _findAnnotation(a, Id::class.java)
        ?.run { PropertyName("_id") }
        ?: super.findNameForSerialization(a)
  }

  override fun findNameForDeserialization(a: Annotated): PropertyName? {
    return _findAnnotation(a, Id::class.java)
        ?.run { PropertyName("_id") }
        ?: super.findNameForDeserialization(a)
  }

  override fun _isIgnorable(a: Annotated?): Boolean {
    return _findAnnotation(a, Ignore::class.java)?.run { true }
        ?: super._isIgnorable(a)
  }

}