package org.goblinframework.rpc.service

import org.goblinframework.api.rpc.ImportService
import org.goblinframework.api.rpc.ServiceVersion

fun calculateServerVersion(annotation: ImportService?): String? {
  return annotation?.run {
    if (this.enable) {
      calculateServerVersion(this.version)
    } else {
      null
    }
  }
}

fun calculateServerVersion(annotation: ServiceVersion?): String? {
  return annotation?.run {
    if (this.enable) {
      val s: String = this.version
      if (s.isBlank()) null else s.trim()
    } else {
      null
    }
  }
}