package org.goblinframework.core.module.management

import org.goblinframework.api.common.Singleton
import org.goblinframework.core.event.EventBusBoss
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Singleton
@RequestMapping("/goblin/event")
class EventManagement private constructor() {

  companion object {
    @JvmField val INSTANCE = EventManagement()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("eventBusBossMXBean", EventBusBoss.INSTANCE)
    return "event/index"
  }
}