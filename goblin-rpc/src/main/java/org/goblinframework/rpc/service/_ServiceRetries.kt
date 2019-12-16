package org.goblinframework.rpc.service

import org.goblinframework.api.rpc.ServiceRetries

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