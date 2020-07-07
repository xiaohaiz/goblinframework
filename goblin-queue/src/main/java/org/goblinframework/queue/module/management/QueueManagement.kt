package org.goblinframework.queue.module.management

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.queue.consumer.builder.QueueConsumerBuilderManager
import org.goblinframework.queue.producer.builder.QueueProducerBuilderManager
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Singleton
@RequestMapping("/goblin/queue")
class QueueManagement private constructor() {

  companion object {
    @JvmField val INSTANCE = QueueManagement()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("queueProducerBuilderManager", QueueProducerBuilderManager.INSTANCE)
    model.addAttribute("queueConsumerBuilderManager", QueueConsumerBuilderManager.INSTANCE)
    return "goblin/queue/index"
  }
}