package org.goblinframework.embedded.handler

import org.goblinframework.embedded.filter.DefaultFilterChain
import org.goblinframework.webmvc.filter.LookupPathFilter
import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.filter.ShallowEtagHeaderFilter
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class FilterSupport : RequestHandlerSupport() {

  private val defaultFilterChain = DefaultFilterChain()

  init {
    defaultFilterChain.addFilter(LookupPathFilter())
    defaultFilterChain.addFilter(ShallowEtagHeaderFilter())
    defaultFilterChain.addFilter(CharacterEncodingFilter(Charsets.UTF_8.name(), true))
  }

  fun createFilterChain(function: () -> Unit): FilterChain {
    val chain = defaultFilterChain.cloneTo()
    chain.addFilter(object : OncePerRequestFilter() {
      override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, filterChain: FilterChain) {
        function()
      }
    })
    return chain
  }

  fun registerFilter(filter: Filter) {
    defaultFilterChain.addFilter(filter)
  }
}