package org.goblinframework.registry.zookeeper

import org.goblinframework.api.annotation.ThreadSafe

@ThreadSafe
internal object ZookeeperClientBuffer {

  private val buffer = mutableMapOf<ZookeeperClientConfig, ZookeeperClient>()

  @Synchronized
  fun create(config: ZookeeperClientConfig): ZookeeperClient {
    var client = buffer[config]
    if (client == null) {
      client = ZookeeperClient(config)
      buffer[config] = client
    }
    client.retain()
    return client
  }

  @Synchronized
  fun remove(config: ZookeeperClientConfig) {
    buffer.remove(config)
  }
}