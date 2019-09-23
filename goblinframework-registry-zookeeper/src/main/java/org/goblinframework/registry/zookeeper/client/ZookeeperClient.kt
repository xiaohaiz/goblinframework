package org.goblinframework.registry.zookeeper.client

import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.ZkConnection
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.registry.zookeeper.module.config.ZookeeperConfig

@GoblinManagedBean(type = "registry.zookeeper")
class ZookeeperClient
internal constructor(private val config: ZookeeperConfig)
  : GoblinManagedObject(), ZookeeperClientMXBean {

  private val transcoder: ZkTranscoder
  private val client: ZkClient

  init {
    val serializerManager = SerializerManager.INSTANCE
    val serializer = serializerManager.getSerializer(config.getSerializer())
    transcoder = ZkTranscoder(serializer)

    val connection = ZkConnection(config.getServers())
    client = ZkClient(connection, Int.MAX_VALUE, transcoder)
  }

  fun nativeClient(): ZkClient {
    return client
  }

  override fun getTranscoder(): ZkTranscoder {
    return transcoder
  }

  override fun disposeBean() {
    client.close()
    transcoder.dispose()
  }
}