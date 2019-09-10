package org.goblinframework.embedded.core.filter

import java.io.IOException
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.servlet.*

class DefaultFilterChain private constructor(filterList: List<Filter>) : FilterChain {

  private val position = AtomicInteger(0)
  private val filterList: LinkedList<Filter> = LinkedList(filterList)

  constructor() : this(emptyList<Filter>())

  fun cloneTo(): DefaultFilterChain {
    return DefaultFilterChain(filterList)
  }

  fun addFilter(filter: Filter) {
    if (filterList.firstOrNull { it === filter } == null) {
      filterList.add(filter)
    }
  }

  @Throws(ServletException::class)
  override fun doFilter(request: ServletRequest, response: ServletResponse) {
    if (position.get() < filterList.size) {
      val filter = filterList[position.getAndIncrement()]
      try {
        filter.doFilter(request, response, this)
      } catch (ex: IOException) {
        throw ServletException(ex)
      }
    }
  }
}
