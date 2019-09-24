package org.goblinframework.registry.zookeeper.provider

import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.apache.zookeeper.data.Stat
import org.goblinframework.api.common.Disposable
import org.goblinframework.api.registry.*
import org.goblinframework.core.util.StringUtils
import org.goblinframework.registry.core.manager.AbstractRegistry
import org.goblinframework.registry.zookeeper.client.ZookeeperClient
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

internal class ZookeeperRegistry
internal constructor(private val client: ZookeeperClient)
  : AbstractRegistry(RegistryLocation(RegistrySystem.ZKP, client.config.getName())), Disposable {

  private val childListenerLock = ReentrantLock()
  private val childListeners = mutableMapOf<String, IdentityHashMap<RegistryChildListener, ZookeeperChildListener>>()
  private val dataListenerLock = ReentrantLock()
  private val dataListeners = mutableMapOf<String, IdentityHashMap<RegistryDataListener, ZookeeperDataListener>>()
  private val stateListenerLock = ReentrantLock()
  private val stateListeners = IdentityHashMap<RegistryStateListener, ZookeeperStateListener>()

  override fun getChildren(path: String): List<String> {
    return try {
      client.nativeClient().getChildren(path)
    } catch (ex: ZkNoNodeException) {
      emptyList()
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun <E : Any> readData(path: String): RegistryData<E?> {
    val stat = Stat()
    val data = try {
      client.nativeClient().readData<Any?>(path, stat)
    } catch (ex: ZkNoNodeException) {
      return RegistryData()
    } ?: return RegistryData()

    val rd = RegistryData<E?>()
    rd.data = data as E
    rd.version = stat.version
    rd.hit = true
    return rd
  }

  override fun createPersistent(path: String) {
    client.nativeClient().createPersistent(path, true)
  }

  override fun createPersistent(path: String, data: Any?) {
    val parent = StringUtils.substringBeforeLast(path, "/")
    if (parent != "/" && !client.nativeClient().exists(parent)) {
      try {
        client.nativeClient().createPersistent(parent)
      } catch (ignore: Exception) {
      }
    }
    client.nativeClient().createPersistent(path, data)
  }

  override fun createEphemeral(path: String) {
    val parent = StringUtils.substringBeforeLast(path, "/")
    if (parent != "/" && !client.nativeClient().exists(parent)) {
      try {
        client.nativeClient().createPersistent(parent)
      } catch (ignore: Exception) {
      }
    }
    client.nativeClient().createEphemeral(path)
  }

  override fun createEphemeral(path: String, data: Any?) {
    val parent = StringUtils.substringBeforeLast(path, "/")
    if (parent != "/" && !client.nativeClient().exists(parent)) {
      try {
        client.nativeClient().createPersistent(parent)
      } catch (ignore: Exception) {
      }
    }
    client.nativeClient().createEphemeral(path, data)
  }

  override fun delete(path: String): Boolean {
    return client.nativeClient().delete(path)
  }

  override fun deleteRecursive(path: String): Boolean {
    return client.nativeClient().deleteRecursive(path)
  }

  override fun subscribeChildListener(path: String, listener: RegistryChildListener) {
    childListenerLock.withLock {
      val map = childListeners.computeIfAbsent(path) { IdentityHashMap() }
      map[listener]?.run { return }
      val zcl = ZookeeperChildListener(listener)
      client.nativeClient().subscribeChildChanges(path, zcl)
      map[listener] = zcl
    }
  }

  override fun unsubscribeChildListener(path: String, listener: RegistryChildListener) {
    childListenerLock.withLock {
      childListeners[path]?.run {
        this.remove(listener)?.let {
          client.nativeClient().unsubscribeChildChanges(path, it)
        }
      }
    }
  }

  override fun subscribeDataListener(path: String, listener: RegistryDataListener) {
    dataListenerLock.withLock {
      val map = dataListeners.computeIfAbsent(path) { IdentityHashMap() }
      map[listener]?.run { return }
      val zdl = ZookeeperDataListener(listener)
      client.nativeClient().subscribeDataChanges(path, zdl)
      map[listener] = zdl
    }
  }

  override fun unsubscribeDataListener(path: String, listener: RegistryDataListener) {
    dataListenerLock.withLock {
      dataListeners[path]?.run {
        this.remove(listener)?.let {
          client.nativeClient().unsubscribeDataChanges(path, it)
        }
      }
    }
  }

  override fun subscribeStateListener(listener: RegistryStateListener) {
    stateListenerLock.withLock {
      stateListeners[listener]?.run { return }
      val zsl = ZookeeperStateListener(listener)
      client.nativeClient().subscribeStateChanges(zsl)
      stateListeners[listener] = zsl
    }
  }

  override fun unsubscribeStateListener(listener: RegistryStateListener) {
    stateListenerLock.withLock {
      stateListeners.remove(listener)?.let {
        client.nativeClient().unsubscribeStateChanges(it)
      }
    }
  }

  override fun dispose() {
    childListenerLock.withLock {
      childListeners.forEach { (t, u) ->
        u.values.forEach { client.nativeClient().unsubscribeChildChanges(t, it) }
      }
    }
    dataListenerLock.withLock {
      dataListeners.forEach { (t, u) ->
        u.values.forEach { client.nativeClient().unsubscribeDataChanges(t, it) }
      }
    }
    stateListenerLock.withLock {
      stateListeners.values.forEach { client.nativeClient().unsubscribeStateChanges(it) }
      stateListeners.clear()
    }
  }
}