package org.goblinframework.rpc.service

import org.goblinframework.api.rpc.ServiceTimeout

fun calculateServiceTimeout(annotation: ServiceTimeout?): Long? {
  return annotation?.run {
    if (this.enable) {
      val t: Int = this.timeout
      if (t < 0) {
        null
      } else {
        val timeout: Long = this.unit.toMillis(t.toLong())
        return if (timeout < 0) null else timeout
      }
    } else {
      null
    }
  }
}