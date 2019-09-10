package org.goblinframework.webmvc.view

import org.apache.commons.lang3.StringUtils
import org.goblinframework.webmvc.servlet.ServletRequest
import org.goblinframework.webmvc.servlet.ServletResponse
import org.springframework.ui.Model
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class AbstractViewResolver : ViewResolver {

  companion object {
    private const val DEFAULT_ORDER = 0
    private const val DEFAULT_CACHE_LIMIT = 8192
    private const val DEFAULT_PREFIX = ""
    private const val DEFAULT_SUFFIX = ""
  }

  private var name: String? = null
  private var order = DEFAULT_ORDER
  private var cacheLimit = DEFAULT_CACHE_LIMIT
  private var prefix = DEFAULT_PREFIX
  private var suffix = DEFAULT_SUFFIX

  private val access: ConcurrentHashMap<String, View> = ConcurrentHashMap(DEFAULT_CACHE_LIMIT)
  private val creation: LinkedHashMap<String, View> = object : LinkedHashMap<String, View>(DEFAULT_CACHE_LIMIT, 0.75f, true) {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, View>?): Boolean {
      if (size > cacheLimit) {
        access.remove(eldest!!.key)
        return true
      } else {
        return false
      }
    }
  }
  private val creationLock = ReentrantLock()

  override fun getOrder(): Int {
    return order
  }

  fun setName(name: String) {
    this.name = name
  }

  fun setOrder(order: Int) {
    this.order = order
  }

  fun setCacheLimit(cacheLimit: Int) {
    this.cacheLimit = cacheLimit
  }

  fun setPrefix(prefix: String) {
    this.prefix = prefix
  }

  fun setSuffix(suffix: String) {
    this.suffix = suffix
  }

  override fun resolve(name: String?): View? {
    var viewName: String = name ?: return null
    if (StringUtils.isNotEmpty(prefix)) {
      viewName = if (!viewName.contains("/")) {
        prefix + viewName
      } else {
        val a = StringUtils.substringBeforeLast(viewName, "/")
        val b = StringUtils.substringAfterLast(viewName, "/")
        "$a/$prefix$b"
      }
    }
    if (StringUtils.isNotEmpty(suffix)) {
      viewName += suffix
    }
    var view: View? = access[viewName]
    if (view == null) {
      creationLock.withLock {
        view = creation[viewName]
        if (view == null) {
          view = createView(viewName)
          if (view == null) {
            view = NilView.instance
          }
          access[viewName] = view!!
          creation[viewName] = view!!
        }
      }
    }
    return if (view === NilView.instance) null else view
  }

  protected abstract fun createView(name: String): View?

  class NilView private constructor() : View {
    companion object {
      val instance = NilView()
    }

    override fun render(model: Model?, request: ServletRequest, response: ServletResponse) {
      throw UnsupportedOperationException()
    }
  }
}