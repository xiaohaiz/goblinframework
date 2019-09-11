package org.goblinframework.schedule.timer

import java.util.concurrent.atomic.AtomicReference

class GoblinTimerEventGenerator : org.goblinframework.core.module.spi.GoblinTimerEventGenerator {

  private val generator = AtomicReference<GoblinTimerEventGeneratorImpl>()

  override fun getOrder(): Int {
    return 0
  }

  @Synchronized
  override fun start() {
    if (generator.get() == null) {
      generator.set(GoblinTimerEventGeneratorImpl())
    }
  }

  override fun stop() {
    generator.getAndSet(null)?.close()
  }

  override fun isRunning(): Boolean {
    return generator.get() != null
  }
}
