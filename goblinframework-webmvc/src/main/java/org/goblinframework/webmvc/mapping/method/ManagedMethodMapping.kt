package org.goblinframework.webmvc.mapping.method

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.webmvc.mapping.MethodMapping
import java.lang.reflect.Method

class ManagedMethodMapping(type: Class<*>, method: Method, private val bean: ContainerManagedBean)
  : MethodMapping(type, method) {

  override fun getBean(): Any {
    return bean.getBean()!!
  }
}
