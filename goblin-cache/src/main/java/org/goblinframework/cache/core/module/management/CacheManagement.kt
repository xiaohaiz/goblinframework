package org.goblinframework.cache.core.module.management

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.cache.core.cache.CacheBuilderManager
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Singleton
@RequestMapping("/goblin/cache")
class CacheManagement private constructor() {

  companion object {
    @JvmField val INSTANCE = CacheManagement()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("cacheBuilderManager", CacheBuilderManager.INSTANCE)
    return "cache/index"
  }
}