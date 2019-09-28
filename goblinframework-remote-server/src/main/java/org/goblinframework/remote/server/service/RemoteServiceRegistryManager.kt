package org.goblinframework.remote.server.service

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.system.GoblinSystem
import org.goblinframework.core.util.HostAndPort
import org.goblinframework.core.util.HttpUtils
import org.goblinframework.core.util.NetworkUtils
import org.goblinframework.registry.core.Registry
import org.goblinframework.registry.core.RegistryPathWatchdog
import org.goblinframework.remote.server.handler.RemoteServer
import java.util.concurrent.TimeUnit

@GoblinManagedBean(type = "RemoteServer")
class RemoteServiceRegistryManager
internal constructor(private val server: RemoteServer, private val registry: Registry)
  : GoblinManagedObject(), RemoteServiceRegistryManagerMXBean {

  private val watchdog: RegistryPathWatchdog
  private val addresses = mutableListOf<HostAndPort>()

  init {
    watchdog = registry.createPathWatchdog().scheduler(1, TimeUnit.MINUTES)
    watchdog.initialize()

    val ts = server.getTransportServer()
    val host = ts.getHost()!!
    val port = ts.getPort()
    if (host == NetworkUtils.ALL_HOST) {
      NetworkUtils.getLocalAddresses().forEach {
        addresses.add(HostAndPort(it, port))
      }
    } else {
      addresses.add(HostAndPort(host, port))
    }
  }

  override fun disposeBean() {
    watchdog.dispose()
  }

  internal fun register(service: RemoteService) {
    generatePathList(service).forEach {
      watchdog.watch(it, true, null)
    }
  }

  internal fun unregister(service: RemoteService) {
    generatePathList(service).forEach {
      watchdog.unwatch(it)
    }
  }

  private fun generatePathList(service: RemoteService): List<String> {
    val pathList = mutableListOf<String>()
    for (address in addresses) {
      val map = LinkedHashMap<String, Any>()
      map["id"] = GoblinSystem.applicationId()
      map["host"] = address.host
      map["port"] = address.port.toString()
      map["group"] = service.id().group
      map["version"] = service.id().version
      val qs = HttpUtils.buildQueryString(map)
      pathList.add("/goblin/remote/service/${service.id().interfaceClass.name}/$qs")
    }
    return pathList
  }
}