package org.goblinframework.webmvc.container

import org.goblinframework.core.container.ContainerRefreshedEvent
import org.goblinframework.core.event.EventBus
import org.springframework.web.context.support.XmlWebApplicationContext

class GoblinWebApplicationContext : XmlWebApplicationContext() {

  override fun finishRefresh() {
    super.finishRefresh()
    EventBus.publish(ContainerRefreshedEvent(this)).awaitUninterruptibly()
  }

}
