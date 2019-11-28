package org.goblinframework.registry.zookeeper

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.StringUtils

@GoblinManagedBean("Registry")
class ZookeeperRegistry
internal constructor(private val client: ZookeeperClient)
  : GoblinManagedObject(), ZookeeperRegistryMXBean {

  private val zkClient: ZkClient = client.getZkClient()

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
}