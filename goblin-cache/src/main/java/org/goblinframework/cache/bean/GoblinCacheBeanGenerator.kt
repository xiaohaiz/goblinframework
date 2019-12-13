package org.goblinframework.cache.bean

import org.goblinframework.cache.annotation.CacheBean
import org.goblinframework.cache.annotation.CacheBeans
import org.goblinframework.cache.annotation.CacheMethod
import org.goblinframework.cache.annotation.CacheParameter
import org.goblinframework.cache.core.cache.CacheLocation
import org.goblinframework.cache.exception.MalformedCacheBeanException
import org.goblinframework.cache.exception.MalformedCacheMethodException
import org.goblinframework.core.util.AnnotationUtils
import java.lang.reflect.Method
import java.lang.reflect.Modifier

fun generateCacheBean(clazz: Class<*>): GoblinCacheBean {
  val annotations = mutableListOf<CacheBean>()
  AnnotationUtils.getAnnotation(clazz, CacheBean::class.java)?.run {
    if (this.enable) {
      annotations.add(this)
    }
  }
  AnnotationUtils.getAnnotation(clazz, CacheBeans::class.java)?.run {
    this.value.filter { it.enable }.forEach { annotations.add(it) }
  }
  if (annotations.isEmpty()) {
    return GoblinCacheBean(emptyMap(), emptyMap())
  }
  val caches = mutableMapOf<Class<*>, GoblinCache>()
  annotations.forEach {
    val c = GoblinCache()
    c.type = it.type.java
    c.location = CacheLocation(it.system, it.connection)
    c.wrapper = it.wrapper
    val expiration = it.expiration
    if (expiration.enable) {
      c.expirationPolicy = expiration.policy
      c.expirationValue = expiration.value
    }
    val cache = it.system.cache(it.connection)
        ?: throw MalformedCacheBeanException("Cache ${it.system}:${it.connection} not found: $clazz")
    c.cache = cache
    caches[c.type!!] = c
  }
  val methods = generateCacheMethods(clazz)
  return GoblinCacheBean(caches, methods)
}

private fun generateCacheMethods(clazz: Class<*>): Map<Method, GoblinCacheMethod> {
  val methods = mutableMapOf<Method, GoblinCacheMethod>()
  clazz.declaredMethods
      .filter {
        val modifier = it.modifiers
        return@filter !Modifier.isStatic(modifier)
      }
      .filter {
        val modifier = it.modifiers
        return@filter !Modifier.isFinal(modifier)
      }
      .filter {
        val modifier = it.modifiers
        return@filter !Modifier.isPrivate(modifier)
      }
      .forEach { method ->
        generateCacheMethod(method)?.run {
          methods[method] = this
        }
      }
  return methods
}

private fun generateCacheMethod(method: Method): GoblinCacheMethod? {
  val cacheMethodAnnotation = method.getAnnotation(CacheMethod::class.java) ?: return null

  val cmpList = mutableListOf<GoblinCacheMethodParameter>()
  val parameterAnnotations = method.parameterAnnotations
  parameterAnnotations.forEachIndexed { index, annotations ->
    annotations.filterIsInstance(CacheParameter::class.java).firstOrNull()?.run {
      val cmp = GoblinCacheMethodParameter()
      cmp.index = index
      cmp.name = this.value
      cmp.multiple = this.multiple
      cmpList.add(cmp)
    }
  }
  if (cmpList.isEmpty()) {
    return null
  }
  var annotationCount = 0
  var multipleCount = 0
  var multipleIndex = -1
  cmpList.forEach {
    annotationCount++
    if (it.multiple) {
      multipleCount++
      multipleIndex = it.index
    }
  }
  if (multipleCount > 1) {
    throw MalformedCacheMethodException("At most one multiple cache parameter: $method")
  }
  if (multipleCount == 1) {
    val multipleParameterType = method.parameterTypes[multipleIndex]
    if (multipleParameterType != Collection::class.java
        && multipleParameterType != Set::class.java
        && multipleParameterType != List::class.java) {
      throw MalformedCacheMethodException("The multiple cache parameter type must be Collection/List/Set: $method")
    }
  }
  if (multipleCount == 1 && method.returnType != Map::class.java) {
    throw MalformedCacheMethodException("The result type of method which has multiple cache parameter must be Map: $method")
  }

  val cm = GoblinCacheMethod()
  cm.type = cacheMethodAnnotation.value.java
  cm.parameterList = cmpList
  cm.annotationCount = annotationCount
  cm.multipleCount = multipleCount
  cm.multipleIndex = multipleIndex
  cm.parameterCount = method.parameterCount
  return cm
}