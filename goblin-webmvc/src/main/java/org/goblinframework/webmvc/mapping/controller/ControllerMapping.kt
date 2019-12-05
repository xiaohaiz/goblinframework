package org.goblinframework.webmvc.mapping.controller

import org.goblinframework.core.util.StringUtils
import org.goblinframework.webmvc.mapping.MalformedMappingException
import org.goblinframework.webmvc.mapping.method.MethodMapping
import org.goblinframework.webmvc.mapping.parameter.*
import org.springframework.web.bind.annotation.RequestMethod
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class ControllerMapping(val requestMethod: RequestMethod,
                        val requestURL: String,
                        val methodMapping: MethodMapping) {

  val urlContainsPathVariable: Boolean
  val urlPathVariableNames: LinkedHashSet<String> = linkedSetOf()
  val uniqueIdentification: String
  val requestHandlerParameters: MutableList<ParameterMapping> = mutableListOf()
  val parameterCounters: IdentityHashMap<Class<out ParameterMapping>, AtomicInteger> = IdentityHashMap()

  init {
    if (!requestURL.contains("{")) {
      urlContainsPathVariable = false
      uniqueIdentification = "${requestMethod.name} $requestURL"
    } else {
      urlContainsPathVariable = true
      val vars = StringUtils.substringsBetween(requestURL, "{", "}")
      vars.forEach {
        if (StringUtils.isBlank(it)) {
          throw MalformedMappingException("Blank path variable name at $methodMapping")
        }
        if (StringUtils.contains(it, " ")) {
          throw MalformedMappingException("Path variable name contains space at $methodMapping")
        }
        if (!urlPathVariableNames.add(it)) {
          throw MalformedMappingException("Duplicate path variable name at $methodMapping")
        }
      }
      var s = requestURL
      for (`var` in vars) {
        s = StringUtils.replaceOnce(s, "{$`var`}", "{}")
      }
      uniqueIdentification = "${requestMethod.name} $s"
    }

    NamedParameterGenerator.generate(methodMapping.method)
        .map { ParameterMappingBuilder.build(it) }
        .forEach { requestHandlerParameters.add(it) }

    requestHandlerParameters.forEach {
      parameterCounters.computeIfAbsent(it.javaClass) { AtomicInteger(0) }
          .incrementAndGet()
    }

    if (parameterCount(ModelParameterMapping::class.java) > 1) {
      throw MalformedMappingException("At most one Model parameter [$this]")
    }

    if (urlPathVariableNames.isNotEmpty()) {
      if (urlPathVariableNames.size != parameterCount(PathVariableParameterMapping::class.java)) {
        throw MalformedMappingException("Invalid path variables [$this]")
      }
      requestHandlerParameters.filter { it is PathVariableParameterMapping }
          .map { it as PathVariableParameterMapping }
          .map { it.name }
          .forEach {
            if (!urlPathVariableNames.contains(it)) {
              throw MalformedMappingException("Path variable '$it' not found in url [$this]")
            }
          }
    }
    if (parameterCount(RequestBodyParameterMapping::class.java) > 0) {
      if (parameterCount(RequestBodyParameterMapping::class.java) > 1) {
        throw MalformedMappingException("At most one @RequestBody parameter [$this]")
      }
      if (requestMethod !== RequestMethod.POST) {
        throw MalformedMappingException("@Request only support POST [$this]")
      }
    }
  }

  private fun parameterCount(parameterMappingType: Class<out ParameterMapping>): Int {
    return parameterCounters[parameterMappingType]?.get() ?: 0
  }

  override fun toString(): String {
    return "$uniqueIdentification $methodMapping"
  }

  fun hasReturnType(): Boolean {
    return methodMapping.returnType !== Void.TYPE
  }

  fun hasRequestBodyParameter(): Boolean {
    return parameterCounters[RequestBodyParameterMapping::class.java]?.get() ?: 0 > 0
  }

  fun getRequestBodyParameter(): RequestBodyParameterMapping? {
    return requestHandlerParameters
        .filter { it is RequestBodyParameterMapping }
        .map { it as RequestBodyParameterMapping }
        .firstOrNull()
  }

  fun hasPathVariableParameter(): Boolean {
    return parameterCounters[PathVariableParameterMapping::class.java]?.get() ?: 0 > 0
  }

  fun getPathVariableParameters(): List<PathVariableParameterMapping> {
    return requestHandlerParameters
        .filter { it is PathVariableParameterMapping }
        .map { it as PathVariableParameterMapping }
        .toList()
  }

  fun hasModelParameter(): Boolean {
    return parameterCounters[ModelParameterMapping::class.java]?.get() ?: 0 > 0
  }
}