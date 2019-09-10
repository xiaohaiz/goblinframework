package org.goblinframework.core.util

import java.util.concurrent.atomic.AtomicBoolean

class StopWatch(autoStart: Boolean = true) : org.apache.commons.lang3.time.StopWatch() {

  private val started = AtomicBoolean()
  private val stopped = AtomicBoolean()

  init {
    if (autoStart) {
      start()
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
}
