package org.goblinframework.webmvc.mapping.method

import java.lang.reflect.Method

class StaticMethodMapping(type: Class<*>, method: Method, private val bean: Any)
  : MethodMapping(type, method) {

  override fun getBean(): Any {
    return bean
  }
}
