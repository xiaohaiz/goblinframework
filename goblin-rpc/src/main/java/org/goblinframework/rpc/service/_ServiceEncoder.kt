package org.goblinframework.rpc.service

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.api.rpc.ServiceEncoder

fun calculateServiceEncoder(annotation: ServiceEncoder?): SerializerMode? {
  return annotation?.run {
    if (this.enable) this.serializer else null
  }
}