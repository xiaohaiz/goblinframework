package org.goblinframework.rpc.service

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.api.rpc.ServiceEncoder
import java.lang.reflect.Method

fun calculateServiceEncoder(interfaceClass: Class<*>, defaultSerializer: SerializerMode): SerializerMode {
  check(interfaceClass.isInterface)
  val annotation = interfaceClass.getAnnotation(ServiceEncoder::class.java)
  val serializer = calculateServiceEncoder(annotation)
  return serializer ?: defaultSerializer
}

fun calculateServiceEncoder(method: Method): SerializerMode? {
  val serviceEncoder = method.getAnnotation(ServiceEncoder::class.java)
  return calculateServiceEncoder(serviceEncoder)
}

fun calculateServiceEncoder(annotation: ServiceEncoder?): SerializerMode? {
  return annotation?.run {
    if (this.enable) this.serializer else null
  }
}