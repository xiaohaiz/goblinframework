package org.goblinframework.rpc.service

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.api.rpc.ServiceEncoder
import java.lang.reflect.Method

fun calculateServiceEncoder(method: Method): SerializerMode? {
  val serviceEncoder = method.getAnnotation(ServiceEncoder::class.java)
  return calculateServiceEncoder(serviceEncoder)
}

fun calculateServiceEncoder(annotation: ServiceEncoder?): SerializerMode? {
  return annotation?.run {
    if (this.enable) this.serializer else null
  }
}