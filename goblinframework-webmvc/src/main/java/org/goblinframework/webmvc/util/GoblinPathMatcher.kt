package org.goblinframework.webmvc.util

import org.springframework.util.AntPathMatcher
import org.springframework.util.PathMatcher

class GoblinPathMatcher private constructor() : PathMatcher by AntPathMatcher(AntPathMatcher.DEFAULT_PATH_SEPARATOR) {

  companion object {
    @JvmField val INSTANCE = GoblinPathMatcher()
  }

}