package org.goblinframework.core.module.management

import org.goblinframework.api.common.Singleton
import org.goblinframework.core.event.EventBusBoss
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

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

  @RequestMapping("channel.do")
  fun channel(model: Model, @RequestParam("channel") channel: String): String {
    EventBusBoss.INSTANCE.lookup(channel)?.run {
      model.addAttribute("eventBusWorkerMXBean", this)
    }
    return "event/channel"
  }
}