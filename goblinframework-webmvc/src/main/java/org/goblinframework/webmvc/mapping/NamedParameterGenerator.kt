package org.goblinframework.webmvc.mapping

import org.springframework.core.DefaultParameterNameDiscoverer
import java.lang.reflect.Method

object NamedParameterGenerator {

  private val discoverer = DefaultParameterNameDiscoverer()

  fun generate(method: Method): List<NamedParameter> {
    if (method.parameterCount == 0) {
      return emptyList()
    }
    val parameters = method.parameters
    val genericParameterTypes = method.genericParameterTypes
    val names = discoverer.getParameterNames(method) ?: throw UnsupportedOperationException()
    assert(parameters.size == names.size)

    val result = arrayListOf<NamedParameter>()
    for (i in parameters.indices) {
      result.add(NamedParameter(parameters[i], genericParameterTypes[i], names[i]))
    }
    return result
  }

}