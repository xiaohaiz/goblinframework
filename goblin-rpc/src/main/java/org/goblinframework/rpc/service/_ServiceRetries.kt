package org.goblinframework.rpc.service

import org.goblinframework.api.rpc.ServiceRetries
import java.lang.reflect.Method

fun calculateServiceRetries(interfaceClass: Class<*>): Int {
  check(interfaceClass.isInterface)
  val annotation = interfaceClass.getAnnotation(ServiceRetries::class.java)
  val retries = calculateServiceRetries(annotation)
  return retries ?: 0
}

fun calculateServiceRetries(method: Method): Int? {
  val serviceRetries = method.getAnnotation(ServiceRetries::class.java)
  return calculateServiceRetries(serviceRetries)
}

fun calculateServiceRetries(annotation: ServiceRetries?): Int? {
  return annotation?.run {
    if (this.enable) {
      val retries: Int = this.retries
      if (retries < 0) null else retries
    } else {
      null
    }
  }
}