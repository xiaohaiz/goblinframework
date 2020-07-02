package org.goblinframework.cache.couchbase.module.config

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.util.StringUtils

class CouchbaseConfigParser internal constructor() : BufferedConfigParser<CouchbaseConfig>() {

  override fun initializeBean() {
    val mapping = ConfigManager.INSTANCE.getMapping()
    parseToMap(mapping, "couchbase", CouchbaseConfigMapper::class.java)
        .map { it.value.also { c -> c.name = it.key } }
        .map { CouchbaseConfig(it) }
        .forEach { putIntoBuffer(it.getName(), it) }
  }

  override fun doProcessConfig(config: CouchbaseConfig) {
    val mapper = config.mapper

    mapper.servers = StringUtils.formalizeServers(mapper.servers, " ", null)
    mapper.bucketPassword ?: kotlin.run { mapper.bucketPassword = "" }
    mapper.openBucketTimeoutInSeconds ?: kotlin.run { mapper.openBucketTimeoutInSeconds = 60 }
    mapper.serializer ?: kotlin.run { mapper.serializer = SerializerMode.HESSIAN2 }
    mapper.compressionThreshold ?: kotlin.run { mapper.compressionThreshold = CompressionThreshold._1M }
    mapper.flushable ?: kotlin.run { mapper.flushable = false }

    if (mapper.servers.isNullOrBlank()) {
      throw IllegalArgumentException("couchbase.${mapper.name}.servers is required")
    }
    if (mapper.bucketName.isNullOrBlank()) {
      throw IllegalArgumentException("couchbase.${mapper.name}.bucketName is required")
    }
  }
}