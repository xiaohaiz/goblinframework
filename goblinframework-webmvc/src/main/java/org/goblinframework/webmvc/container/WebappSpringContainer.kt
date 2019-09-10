package org.goblinframework.webmvc.container

import org.bson.types.ObjectId
import org.goblinframework.core.container.ContainerRefreshedEvent
import org.goblinframework.core.container.SpringContainerId
import org.goblinframework.core.event.EventBus
import org.springframework.web.context.support.XmlWebApplicationContext

class WebappSpringContainer : XmlWebApplicationContext(), SpringContainerId {

  private val uniqueId = ObjectId().toHexString()

  override fun uniqueId(): String {
    return uniqueId
  }

  override fun finishRefresh() {
    super.finishRefresh()
    EventBus.publish(ContainerRefreshedEvent(this)).awaitUninterruptibly()
  }

}
