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

  @RequestMapping("worker.do")
  fun worker(model: Model, @RequestParam("workerId") workerId: String): String {
    EventBusBoss.INSTANCE.getEventBusWorkerList()
        .firstOrNull { it.getId() == workerId }?.run {
          model.addAttribute("eventBusWorkerMXBean", this)
        }
    return "event/worker"
  }

  @RequestMapping("listener.do")
  fun listener(model: Model,
               @RequestParam("workerId") workerId: String,
               @RequestParam("listenerId") listenerId: String): String {
    model.addAttribute("workerId", workerId)
    EventBusBoss.INSTANCE.getEventBusWorkerList()
        .firstOrNull { it.getId() == workerId }?.run {
          val worker = this
          worker.getEventListenerList()
              .firstOrNull { it.getId() == listenerId }?.run {
                model.addAttribute("eventListenerMXBean", this)
              }
        }
    return "event/listener"
  }
}