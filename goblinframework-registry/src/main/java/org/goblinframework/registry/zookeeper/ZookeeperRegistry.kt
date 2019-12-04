package org.goblinframework.registry.zookeeper

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.StringUtils
import org.goblinframework.registry.listener.*
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@GoblinManagedBean("Registry")
class ZookeeperRegistry
internal constructor(private val client: ZookeeperClient)
  : GoblinManagedObject(), ZookeeperRegistryMXBean {

  private val zkClient: ZkClient = client.getZkClient()

  private val childListenerLock = ReentrantLock()
  private val childListeners = mutableMapOf<String, IdentityHashMap<RegistryChildListener, ZookeeperChildListener>>()
  private val dataListenerLock = ReentrantLock()
  private val dataListeners = mutableMapOf<String, IdentityHashMap<RegistryDataListener, ZookeeperDataListener>>()
  private val stateListenerLock = ReentrantLock()
  private val stateListeners = IdentityHashMap<RegistryStateListener, ZookeeperStateListener>()

  fun createKeeper(): ZookeeperRegistryPathKeeper {
    return ZookeeperRegistryPathKeeper(this)
  }

  fun createWatcher(): ZookeeperRegistryPathWatcher {
    return ZookeeperRegistryPathWatcher(this)
  }

  fun exists(path: String): Boolean {
    return zkClient.exists(path)
  }

  fun getChildren(path: String): List<String> {
    return try {
      zkClient.getChildren(path)
    } catch (ex: ZkNoNodeException) {
      emptyList()
    }
  }

  fun <E : Any?> readData(path: String): E? {
    return zkClient.readData<E?>(path, true)
  }

  fun writeData(path: String, data: Any?) {
    zkClient.writeData(path, data)
  }

  fun createPersistent(path: String) {
    zkClient.createPersistent(path, true)
  }

  fun createPersistent(path: String, data: Any?) {
    var parent = StringUtils.substringBeforeLast(path, "/")
    parent = StringUtils.defaultIfBlank(parent, "/")
    if (parent != "/" && !zkClient.exists(parent)) {
      try {
        zkClient.createPersistent(parent, true)
      } catch (ignore: Exception) {
      }
    }
    zkClient.createPersistent(path, data)
  }

  fun createEphemeral(path: String) {
    var parent = StringUtils.substringBeforeLast(path, "/")
    parent = StringUtils.defaultIfBlank(parent, "/")
    if (parent != "/" && !zkClient.exists(parent)) {
      try {
        zkClient.createPersistent(parent, true)
      } catch (ignore: Exception) {
      }
    }
    zkClient.createEphemeral(path)
  }

  fun createEphemeral(path: String, data: Any?) {
    var parent = StringUtils.substringBeforeLast(path, "/")
    parent = StringUtils.defaultIfBlank(parent, "/")
    if (parent != "/" && !zkClient.exists(parent)) {
      try {
        zkClient.createPersistent(parent, true)
      } catch (ignore: Exception) {
      }
    }
    zkClient.createEphemeral(path, data)
  }

  fun delete(path: String): Boolean {
    return zkClient.delete(path)
  }

  fun deleteRecursive(path: String): Boolean {
    return zkClient.deleteRecursive(path)
  }

  fun subscribeChildListener(path: String, listener: RegistryChildListener) {
    childListenerLock.withLock {
      val map = childListeners.computeIfAbsent(path) { IdentityHashMap() }
      map[listener]?.run { return }
      val zcl = ZookeeperChildListener(listener)
      zkClient.subscribeChildChanges(path, zcl)
      map[listener] = zcl
    }
  }

  fun unsubscribeChildListener(path: String, listener: RegistryChildListener) {
    childListenerLock.withLock {
      childListeners[path]?.run {
        this.remove(listener)?.let {
          zkClient.unsubscribeChildChanges(path, it)
        }
      }
    }
  }

  fun subscribeDataListener(path: String, listener: RegistryDataListener) {
    dataListenerLock.withLock {
      val map = dataListeners.computeIfAbsent(path) { IdentityHashMap() }
      map[listener]?.run { return }
      val zdl = ZookeeperDataListener(listener)
      zkClient.subscribeDataChanges(path, zdl)
      map[listener] = zdl
    }
  }

  fun unsubscribeDataListener(path: String, listener: RegistryDataListener) {
    dataListenerLock.withLock {
      dataListeners[path]?.run {
        this.remove(listener)?.let {
          zkClient.unsubscribeDataChanges(path, it)
        }
      }
    }
  }

  fun subscribeStateListener(listener: RegistryStateListener) {
    stateListenerLock.withLock {
      stateListeners[listener]?.run { return }
      val zsl = ZookeeperStateListener(listener)
      zkClient.subscribeStateChanges(zsl)
      stateListeners[listener] = zsl
    }
  }

  fun unsubscribeStateListener(listener: RegistryStateListener) {
    stateListenerLock.withLock {
      stateListeners.remove(listener)?.let {
        zkClient.unsubscribeStateChanges(it)
      }
    }
  }

  override fun disposeBean() {
    childListenerLock.withLock {
      childListeners.forEach { (t, u) ->
        u.values.forEach { zkClient.unsubscribeChildChanges(t, it) }
      }
    }
    dataListenerLock.withLock {
      dataListeners.forEach { (t, u) ->
        u.values.forEach { zkClient.unsubscribeDataChanges(t, it) }
      }
    }
    stateListenerLock.withLock {
      stateListeners.values.forEach { zkClient.unsubscribeStateChanges(it) }
      stateListeners.clear()
    }
  }
}