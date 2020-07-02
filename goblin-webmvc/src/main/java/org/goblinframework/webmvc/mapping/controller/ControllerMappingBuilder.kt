package org.goblinframework.webmvc.mapping.controller

import org.goblinframework.webmvc.mapping.MalformedMappingException
import org.goblinframework.webmvc.mapping.method.MethodMapping
import java.util.*

object ControllerMappingBuilder {

  fun build(methodMappings: List<MethodMapping>): List<ControllerMapping> {
    val handlerList = ArrayList<ControllerMapping>()
    methodMappings.forEach {
      val mapping = it
      val methods = RequestMethodGenerator.generate(it)
      it.allowMethods.addAll(methods)
      val urls = RequestUrlGenerator.generate(it)

      val handlers = ArrayList<ControllerMapping>(methods.size * urls.size)
      methods.forEach {
        val method = it
        urls.forEach {
          handlers.add(ControllerMapping(method, it, mapping))
        }
      }
      handlerList.addAll(handlers)
    }
    val map = HashMap<String, ControllerMapping>()
    handlerList.forEach { handler ->
      val id = handler.uniqueIdentification
      val previous = map.put(id, handler)
      if (previous != null) {
        throw MalformedMappingException("Ambiguous request handler mapping found: $previous")
      }
    }
    return handlerList
  }

}