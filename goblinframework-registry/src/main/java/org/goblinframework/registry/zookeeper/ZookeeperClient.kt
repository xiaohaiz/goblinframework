package org.goblinframework.registry.zookeeper

import org.I0Itec.zkclient.IZkConnection
import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.ZkConnection
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.ReflectionUtils

@GoblinManagedBean("Registry")
class ZookeeperClient internal constructor(setting: ZkClientSetting)
  : GoblinManagedObject(), ZookeeperClientMXBean {

  private val transcoder = ZkTranscoder(setting.serializer())
  private val client: ZkClient

  init {
    var connection: IZkConnection = ZkConnection(setting.addresses(), setting.sessionTimeout())
    val interceptor = ZkConnectionInterceptor(connection)
    connection = ReflectionUtils.createProxy(IZkConnection::class.java, interceptor)
    client = ZkClient(connection, setting.connectionTimeout(), transcoder)
  }

  fun getZkClient(): ZkClient {
    return client
  }

  override fun getTranscoder(): ZkTranscoderMXBean {
    return transcoder
  }

  override fun disposeBean() {
    client.unsubscribeAll()
    client.close()
    transcoder.dispose()
  }
}