package org.goblinframework.webmvc.mapping

import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import java.lang.reflect.Method
import java.util.*

abstract class MethodMapping protected constructor(val type: Class<*>, val method: Method) {

  val returnType = method.returnType
  val restful = type.isAnnotationPresent(ResponseBody::class.java)
      || method.isAnnotationPresent(ResponseBody::class.java)
  val allowMethods: EnumSet<RequestMethod> = EnumSet.noneOf(RequestMethod::class.java)

  init {
    if (restful && returnType === Void.TYPE) {
      throw MalformedMappingException("REST method must not return void")
    }
    if (!restful && returnType !== Void.TYPE && returnType !== String::class.java) {
      throw MalformedMappingException("Non REST can only return String")
    }
  }

  abstract fun getBean(): Any

  override fun toString(): String {
    return "${type.name}.${method.name}"
  }
}