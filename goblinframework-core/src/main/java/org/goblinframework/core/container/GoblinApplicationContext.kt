package org.goblinframework.core.container

import org.springframework.beans.BeansException
import org.springframework.context.support.ClassPathXmlApplicationContext

import java.util.concurrent.atomic.AtomicBoolean

class GoblinApplicationContext @Throws(BeansException::class)
constructor(vararg configLocations: String)
  : ClassPathXmlApplicationContext(configLocations, false) {

  private val refreshed = AtomicBoolean()
  private val started = AtomicBoolean()
  private val stopped = AtomicBoolean()
  private val closed = AtomicBoolean()

  init {
    refresh()
    start()
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
    }
  }
}
