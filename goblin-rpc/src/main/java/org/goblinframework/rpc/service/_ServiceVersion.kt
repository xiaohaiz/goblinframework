package org.goblinframework.rpc.service

import org.goblinframework.api.rpc.ExposeService
import org.goblinframework.api.rpc.ImportService
import org.goblinframework.api.rpc.ServiceVersion
import org.goblinframework.core.util.StringUtils
import java.lang.reflect.Field

fun calculateServerVersion(field: Field): String? {
  val serviceVersion = field.getAnnotation(ServiceVersion::class.java)
  val version = calculateServerVersion(serviceVersion)
  if (version != null) {
    return version
  }
  val importService = field.getAnnotation(ImportService::class.java)
  return calculateServerVersion(importService)
}

fun calculateServerVersion(annotation: ExposeService): String {
  require(annotation.enable)
  val version: String? = calculateServerVersion(annotation.version)
  return version ?: calculateServerVersion(annotation.interfaceClass.java)
}

fun calculateServerVersion(interfaceClass: Class<*>): String {
  check(interfaceClass.isInterface)
  val serviceVersion = interfaceClass.getAnnotation(ServiceVersion::class.java)
  val version = calculateServerVersion(serviceVersion)
  return StringUtils.defaultString(version, ServiceVersion.DEFAULT_VERSION)
}

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