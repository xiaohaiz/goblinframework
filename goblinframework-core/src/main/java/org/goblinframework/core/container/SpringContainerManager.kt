package org.goblinframework.core.container

import org.goblinframework.api.core.ISpringContainerManager
import org.goblinframework.api.core.Install
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean(type = "core")
class SpringContainerManager private constructor()
  : GoblinManagedObject(), ISpringContainerManager, SpringContainerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = SpringContainerManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val containers = LinkedHashMap<String, SpringContainer>()

  private val beanPostProcessors = mutableListOf<SpringContainerBeanPostProcessor>()

  override fun createStandaloneContainer(vararg configLocations: String): SpringContainer {
    val container = StandaloneSpringContainer(*configLocations)
    val id = container.uniqueId()
    return lock.read { containers[id]!! }
  }

  fun registerBeanPostProcessor(postProcessor: SpringContainerBeanPostProcessor) {
    synchronized(beanPostProcessors) {
      beanPostProcessors.add(postProcessor)
    }
  }

  fun register(ctx: ApplicationContext) {
    if (ctx !is SpringContainerId) {
      throw UnsupportedOperationException()
    }
    val id = (ctx as SpringContainerId).uniqueId()
    lock.write { containers.putIfAbsent(id, SpringContainer(ctx)) }
  }

  fun unregister(ctx: ApplicationContext) {
    if (ctx !is SpringContainerId) {
      throw UnsupportedOperationException()
    }
    val id = (ctx as SpringContainerId).uniqueId()
    lock.write { containers.remove(id) }?.dispose()
  }

  fun getBeanPostProcessors(): List<SpringContainerBeanPostProcessor> {
    return synchronized(beanPostProcessors) {
      beanPostProcessors.sortedBy { it.order }.toMutableList()
    }
  }

  override fun disposeBean() {
    lock.write {
      containers.values.map { it.applicationContext }
          .filterIsInstance<ConfigurableApplicationContext>()
          .forEach { it.close() }
      containers.clear()
    }
  }

  @Install
  class Installer : ISpringContainerManager by INSTANCE
}