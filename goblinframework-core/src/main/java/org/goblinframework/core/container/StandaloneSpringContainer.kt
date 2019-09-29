package org.goblinframework.core.container

import org.bson.types.ObjectId
import org.goblinframework.core.event.EventBus
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.util.ThreadUtils
import org.springframework.beans.BeansException
import org.springframework.context.support.ClassPathXmlApplicationContext
import reactor.core.publisher.MonoProcessor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

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
    addBeanFactoryPostProcessor {
      it.addBeanPostProcessor(SpringBeanPostProcessorDelegator())
    }
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
    val cause = AtomicReference<Throwable?>()
    val latch = CountDownLatch(1)
    val publisher = EventBus.publish2(ContainerRefreshedEvent(this))
    val processor = MonoProcessor.create<GoblinEventContext>()
    processor.subscribe(null,
        {
          cause.set(it)
          latch.countDown()
        },
        {
          latch.countDown()
        })
    publisher.subscribe(processor)
    ThreadUtils.awaitUninterruptibly(latch)
    cause.getAndSet(null)?.run {
      throw this
    }
  }
}
