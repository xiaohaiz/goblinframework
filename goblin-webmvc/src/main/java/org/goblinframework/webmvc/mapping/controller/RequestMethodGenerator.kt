package org.goblinframework.webmvc.mapping.controller

import org.goblinframework.webmvc.mapping.method.MethodMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.*

object RequestMethodGenerator {

  fun generate(methodMapping: MethodMapping): EnumSet<RequestMethod> {
    val type = methodMapping.type
    val ta = type.getAnnotation(RequestMapping::class.java)!!
    val tms = EnumSet.noneOf(RequestMethod::class.java)
    tms.addAll(ta.method)
    if (tms.isEmpty()) {
      tms.add(RequestMethod.GET)
      tms.add(RequestMethod.POST)
      tms.add(RequestMethod.OPTIONS)
    }
    val method = methodMapping.method
    val ma = method.getAnnotation(RequestMapping::class.java)!!
    val mms = EnumSet.noneOf(RequestMethod::class.java)
    mms.addAll(ma.method)
    return if (mms.isEmpty()) tms else mms
  }

}