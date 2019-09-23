package org.goblinframework.webmvc.container

import org.bson.types.ObjectId
import org.goblinframework.api.event.EventBus
import org.goblinframework.core.container.ContainerRefreshedEvent
import org.goblinframework.core.container.SpringContainerId
import org.goblinframework.core.container.SpringContainerManager
import org.springframework.web.context.support.XmlWebApplicationContext
import java.util.concurrent.atomic.AtomicBoolean

class ServletSpringContainer : XmlWebApplicationContext(), SpringContainerId {

  private val uniqueId = ObjectId().toHexString()
  private val closed = AtomicBoolean()

  init {
    SpringContainerManager.INSTANCE.register(this)
  }

  override fun uniqueId(): String {
    return uniqueId
  }

  override fun close() {
    if (closed.compareAndSet(false, true)) {
      Exception().printStackTrace()
      super.close()
      SpringContainerManager.INSTANCE.unregister(this)
    }
  }

  override fun finishRefresh() {
    super.finishRefresh()
    EventBus.publish(ContainerRefreshedEvent(this)).awaitUninterruptibly()
  }

}
