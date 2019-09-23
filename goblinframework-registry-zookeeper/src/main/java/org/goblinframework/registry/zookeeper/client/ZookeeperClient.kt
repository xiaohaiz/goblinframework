package org.goblinframework.registry.zookeeper.client

import org.I0Itec.zkclient.IZkConnection
import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.ZkConnection
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.util.ReflectionUtils
import org.goblinframework.registry.zookeeper.interceptor.ZkConnectionInterceptor
import org.goblinframework.registry.zookeeper.module.config.ZookeeperConfig

@GoblinManagedBean(type = "registry.zookeeper")
class ZookeeperClient
internal constructor(val config: ZookeeperConfig)
  : GoblinManagedObject(), ZookeeperClientMXBean {

  private val transcoder: ZkTranscoder
  private val client: ZkClient

  init {
    val serializerManager = SerializerManager.INSTANCE
    val serializer = serializerManager.getSerializer(config.getSerializer())
    transcoder = ZkTranscoder(serializer)

    var connection: IZkConnection = ZkConnection(config.getServers())
    val interceptor = ZkConnectionInterceptor(config.getName(), connection)
    connection = ReflectionUtils.createProxy(IZkConnection::class.java, interceptor)
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