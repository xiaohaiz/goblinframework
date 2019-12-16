package org.goblinframework.remote.server.service

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.HostAndPort
import org.goblinframework.core.util.NetworkUtils
import org.goblinframework.registry.zookeeper.ZookeeperRegistryPathKeeper
import org.goblinframework.remote.core.service.RemoteServiceId
import org.goblinframework.remote.server.module.exception.DuplicateServiceException
import org.goblinframework.remote.server.transport.RemoteTransportServerManager
import org.goblinframework.rpc.registry.RpcRegistryManager
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.service")
class RemoteServiceManager private constructor()
  : GoblinManagedObject(), RemoteServiceManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServiceManager()

    fun createRemoteService(vararg beans: Any) {
      for (bean in beans) {
        val ids = RemoteServiceIdGenerator.generate(bean.javaClass)
        if (ids.isEmpty()) {
          continue
        }
        INSTANCE.createStaticService(bean, ids)
      }
    }
  }

  private val addresses = mutableListOf<HostAndPort>()
  private val keeperReference = AtomicReference<ZookeeperRegistryPathKeeper?>()
  private val lock = ReentrantReadWriteLock()
  private val services = mutableMapOf<RemoteServiceId, RemoteService>()

  override fun initializeBean() {
    RemoteTransportServerManager.INSTANCE.getRemoteTransportServer()?.run {
      val port = this.getTransportServer().getPort()!!
      val host = this.getTransportServer().getHost()!!
      if (host == NetworkUtils.ALL_HOST) {
        NetworkUtils.getLocalAddresses().forEach { addresses.add(HostAndPort(it, port)) }
      } else {
        addresses.add(HostAndPort(host, port))
      }
    }
    RpcRegistryManager.INSTANCE.getRpcRegistry()?.run {
      val keeper = this.createKeeper().scheduler(1, TimeUnit.MINUTES)
      keeper.initialize()
      keeperReference.set(keeper)
    }
  }

  fun createStaticService(bean: Any, ids: List<RemoteServiceId>) {
    lock.write {
      for (id in ids) {
        services[id]?.run {
          val type = ClassUtils.filterCglibProxyClass(bean.javaClass)
          val errMsg = "[serviceType=${type.name},serviceId=${id.asText()}]"
          throw DuplicateServiceException(errMsg)
        }
        val service = RemoteServiceStaticImpl(id, addresses, bean)
        service.initialize(keeperReference.get())
        service.publish()
        services[id] = service
      }
    }
  }

  fun createManagedServices(bean: ContainerManagedBean, ids: List<RemoteServiceId>) {
    lock.write {
      for (id in ids) {
        services[id]?.run {
          val name = bean.beanName
          val type = ClassUtils.filterCglibProxyClass(bean.type!!)
          val errMsg = "[serviceName=$name,serviceType=${type.name},serviceId=${id.asText()}]"
          throw DuplicateServiceException(errMsg)
        }
        val service = RemoteServiceManagedImpl(id, addresses, bean)
        service.initialize(keeperReference.get())
        service.publish()
        services[id] = service
      }
    }
  }

  fun getRemoteService(serviceId: RemoteServiceId): RemoteService? {
    return lock.read { services[serviceId] }
  }

  fun publishAll() {
    lock.read { services.values.forEach { it.publish() } }
  }

  fun unregisterAll() {
    lock.read { services.values.forEach { it.unregister() } }
  }

  override fun disposeBean() {
    lock.write {
      services.values.forEach {
        it.unregister()
        it.dispose()
      }
      services.clear()
    }
    keeperReference.getAndSet(null)?.dispose()
  }
}