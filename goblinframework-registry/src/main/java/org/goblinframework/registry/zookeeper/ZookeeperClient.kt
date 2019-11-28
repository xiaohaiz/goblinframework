package org.goblinframework.registry.zookeeper

import org.I0Itec.zkclient.IZkConnection
import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.ZkConnection
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.GoblinReferenceCount
import org.goblinframework.core.util.ReflectionUtils

@GoblinManagedBean("Registry")
class ZookeeperClient
internal constructor(private val config: ZookeeperClientConfig)
  : GoblinManagedObject(), ZookeeperClientMXBean {

  private val transcoder: ZkTranscoder
  private val client: ZkClient
  private val referenceCount = GoblinReferenceCount()

  init {
    val serializer = SerializerManager.INSTANCE.getSerializer(config.serializer)
    transcoder = ZkTranscoder(serializer)
    var connection: IZkConnection = ZkConnection(config.addresses, config.sessionTimeout)
    val interceptor = ZkConnectionInterceptor(connection)
    connection = ReflectionUtils.createProxy(IZkConnection::class.java, interceptor)
    client = ZkClient(connection, config.connectionTimeout, transcoder)
  }

  fun getZkClient(): ZkClient {
    return client
  }

  override fun getTranscoder(): ZkTranscoderMXBean {
    return transcoder
  }

  internal fun retain() {
    referenceCount.retain()
  }

  override fun dispose() {
    if (referenceCount.release()) {
      ZookeeperClientBuffer.remove(config)
      super.dispose()
    }
  }

  override fun disposeBean() {
    client.unsubscribeAll()
    client.close()
    transcoder.dispose()
  }
}