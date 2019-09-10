package org.goblinframework.embedded.core.handler

import org.apache.commons.lang3.StringUtils
import org.goblinframework.webmvc.handler.RequestHandlerManager
import java.util.concurrent.ConcurrentHashMap

abstract class RequestHandlerSupport protected constructor()
  : StaticResourceSupport() {

  private val pool = ConcurrentHashMap<String, RequestHandlerManager>()

  fun registerRequestHandlerManager(requestHandlerManager: RequestHandlerManager,
                                    vararg names: String) {
    names.asList()
        .filterNot { it.isBlank() }
        .map { it.trim() }
        .map { if (it.startsWith(".")) it else ".$it" }
        .forEach { pool[it] = requestHandlerManager }
  }

  fun getRequestHandlerManager(lookupPath: String): RequestHandlerManager? {
    val name = ".${StringUtils.substringAfterLast(lookupPath, ".")}"
    return pool[name]
  }
}