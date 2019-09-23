package org.goblinframework.core.container

import org.bson.types.ObjectId
import org.goblinframework.api.event.EventBus
import org.springframework.beans.BeansException
import org.springframework.context.support.ClassPathXmlApplicationContext

import java.util.concurrent.atomic.AtomicBoolean

class StandaloneSpringContainer @Throws(BeansException::class)
constructor(vararg configLocations: String)
  : ClassPathXmlApplicationContext(configLocations, false), SpringContainerId {

  private val uniqueId = ObjectId().toHexString()
  private val refreshed = AtomicBoolean()
  private val started = AtomicBoolean()
  private val stopped = AtomicBoolean()
  private val closed = AtomicBoolean()

  init {
    SpringContainerManager.INSTANCE.register(this)
    refresh()
    start()
  }

  override fun uniqueId(): String {
    return uniqueId
  }

  @Throws(BeansException::class, IllegalStateException::class)
  override fun refresh() {
    if (refreshed.compareAndSet(false, true)) {
      super.refresh()
    }
  }

  override fun start() {
    if (started.compareAndSet(false, true)) {
      super.start()
    }
  }

  override fun stop() {
    if (stopped.compareAndSet(false, true)) {
      super.stop()
    }
  }

  override fun close() {
    stop()
    if (closed.compareAndSet(false, true)) {
      super.close()
      SpringContainerManager.INSTANCE.unregister(this)
    }
  }

  override fun finishRefresh() {
    super.finishRefresh()
    EventBus.publish(ContainerRefreshedEvent(this)).awaitUninterruptibly()
  }
}
