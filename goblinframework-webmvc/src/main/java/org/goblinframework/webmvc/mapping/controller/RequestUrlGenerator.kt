package org.goblinframework.webmvc.mapping.controller

import org.apache.commons.lang3.StringUtils
import org.goblinframework.webmvc.mapping.MalformedMappingException
import org.goblinframework.webmvc.mapping.method.MethodMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

object RequestUrlGenerator {

  fun generate(methodMapping: MethodMapping): Set<String> {
    val type = methodMapping.type
    val ta = type.getAnnotation(RequestMapping::class.java)
    val prefixes = HashSet<String>()
    ta.value
        .filterNot { StringUtils.isBlank(it) }
        .map { StringUtils.trim(it) }
        .forEach { prefixes.add(it) }

    val method = methodMapping.method
    val ma = method.getAnnotation(RequestMapping::class.java)
    val values = HashSet<String>()
    if (ma != null) {
      ma.value
          .filterNot { StringUtils.isBlank(it) }
          .map { StringUtils.trim(it) }
          .forEach { values.add(it) }
    }

    val urls = HashSet<String>()
    if (!prefixes.isEmpty()) {
      prefixes.forEach { p ->
        values.forEach { v ->
          var url = "/$p/$v"
          while (StringUtils.contains(url, "//")) {
            url = StringUtils.replace(url, "//", "/")
          }
          urls.add(url)
        }
      }
    } else {
      values.forEach { v ->
        var url = "/$v"
        while (StringUtils.contains(url, "//")) {
          url = StringUtils.replace(url, "//", "/")
        }
        urls.add(url)
      }
    }
    if (urls.isEmpty()) {
      throw MalformedMappingException("No request mapping: $methodMapping")
    }
    urls.forEach { u ->
      if (StringUtils.contains(u, "*") || StringUtils.contains(u, "?")) {
        throw MalformedMappingException("Invalid URL [$u]")
      }
    }
    urls.forEach { u ->
      val l = StringUtils.countMatches(u, '{')
      val r = StringUtils.countMatches(u, '}')
      if (l != r) {
        throw MalformedMappingException("Invalid URL [$u]")
      }
    }
    return urls
  }

}