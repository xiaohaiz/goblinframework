package org.goblinframework.webmvc.util

import org.goblinframework.http.util.HttpUtils
import org.springframework.web.util.UrlPathHelper

import javax.servlet.http.HttpServletRequest

class GoblinUrlPathHelper private constructor() : UrlPathHelper() {

  companion object {
    val INSTANCE = GoblinUrlPathHelper()
  }

  init {
    isUrlDecode = true
    defaultEncoding = Charsets.UTF_8.name()
    setAlwaysUseFullPath(false)
    setRemoveSemicolonContent(true)
  }

  override fun getLookupPathForRequest(request: HttpServletRequest): String? {
    val path = super.getLookupPathForRequest(request)
    return HttpUtils.compactContinuousSlashes(path)
  }
}
