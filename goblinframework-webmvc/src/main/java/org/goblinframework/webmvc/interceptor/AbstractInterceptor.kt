package org.goblinframework.webmvc.interceptor

import org.goblinframework.core.util.ArrayUtils
import org.goblinframework.core.util.StringUtils
import org.goblinframework.webmvc.handler.RequestHandler
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.goblinframework.webmvc.util.DefaultPathMatcher
import java.util.concurrent.atomic.AtomicReference

abstract class AbstractInterceptor protected constructor() : Interceptor {

  private var includes: Array<String>? = null
  private var excludes: Array<String>? = null
  private val matchingEverything = AtomicReference<Boolean>()
  private var order: Int? = null

  fun setOrder(order: Int) {
    this.order = order
  }

  override fun getOrder(): Int {
    return order ?: 0
  }

  override fun getIncludePatterns(): Array<String> {
    return includes ?: emptyArray()
  }

  override fun getExcludePatterns(): Array<String> {
    return excludes ?: emptyArray()
  }

  @Synchronized
  override fun setIncludePatterns(includePatterns: String) {
    if (includePatterns.isBlank()) {
      return
    }
    includes = StringUtils.split(includePatterns.trim(), ",")
        .filterNot { it.isBlank() }
        .map { it.trim() }
        .toTypedArray()
    matchingEverything.set(null)
  }

  @Synchronized
  override fun setExcludePatterns(excludePatterns: String) {
    if (excludePatterns.isBlank()) {
      return
    }
    excludes = StringUtils.split(excludePatterns.trim(), ",")
        .filterNot { it.isBlank() }
        .map { it.trim() }
        .toTypedArray()
    matchingEverything.set(null)
  }

  override fun matches(lookupPath: String): Boolean {
    if (isMatchingEverything()) {
      return true
    }
    val pathMatcher = DefaultPathMatcher.INSTANCE
    val includePatterns = getIncludePatterns()
    val excludePatterns = getExcludePatterns()
    for (pattern in excludePatterns) {
      if (pathMatcher.match(pattern, lookupPath)) {
        return false
      }
    }
    if (includePatterns.isEmpty()) {
      return true
    } else {
      for (pattern in includePatterns) {
        if (pathMatcher.match(pattern, lookupPath)) {
          return true
        }
      }
      return false
    }
  }

  override fun preHandle(request: ServletRequest, response: ServletResponse, handler: RequestHandler): Boolean {
    return true
  }

  override fun postHandle(request: ServletRequest, response: ServletRequest, handler: RequestHandler) {
  }

  override fun afterCompletion(request: ServletRequest, response: ServletRequest, handler: RequestHandler, cause: Throwable?) {
  }

  @Synchronized
  private fun isMatchingEverything(): Boolean {
    if (matchingEverything.get() != null) {
      return matchingEverything.get()
    }
    val includePatterns = getIncludePatterns()
    val excludePatterns = getExcludePatterns()
    if (ArrayUtils.isEmpty(includePatterns) && ArrayUtils.isEmpty(excludePatterns)) {
      matchingEverything.set(true)
      return true
    }
    if (ArrayUtils.isNotEmpty(excludePatterns)) {
      matchingEverything.set(false)
      return false
    }
    for (includePattern in includePatterns) {
      if (StringUtils.equals(StringUtils.trim(includePattern), "/**")) {
        matchingEverything.set(true)
        return true
      }
    }
    matchingEverything.set(false)
    return false
  }
}