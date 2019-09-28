package org.goblinframework.remote.client.module.config

import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.registry.core.RegistryLocation

class RemoteClientConfigParser internal constructor()
  : BufferedConfigParser<RemoteClientConfig>() {

  companion object {
    private const val CONFIG_NAME = "RemoteClientConfig"
  }

  override fun initializeBean() {
    val configMapping = ConfigManager.INSTANCE.getMapping()
    (configMapping["remote"] as? Map<*, *>)?.run {
      val remote = this
      (remote["client"] as? Map<*, *>)?.run {
        val client = this
        val mapper = JsonMapper.getDefaultObjectMapper()
        val cm = mapper.convertValue(client, RemoteClientConfigMapper::class.java)
        putIntoBuffer(CONFIG_NAME, RemoteClientConfig(cm))
      }
    }
  }

  internal fun getRemoteClientConfig(): RemoteClientConfig? {
    return getFromBuffer(CONFIG_NAME)
  }

  override fun doProcessConfig(config: RemoteClientConfig) {
    val mapper = config.mapper
    mapper.registry?.run {
      config.registryLocation = RegistryLocation.parse(this)
    }
  }
}