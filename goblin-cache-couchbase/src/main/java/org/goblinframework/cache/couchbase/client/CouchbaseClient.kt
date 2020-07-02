package org.goblinframework.cache.couchbase.client

import com.couchbase.client.java.Bucket
import com.couchbase.client.java.bucket.BucketType
import org.goblinframework.cache.couchbase.module.config.CouchbaseConfig
import org.goblinframework.cache.couchbase.transcoder.CouchbaseTranscoder
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.system.GoblinSystem
import org.goblinframework.core.system.RuntimeMode
import java.util.concurrent.TimeUnit

@GoblinManagedBean("CacheCouchbase")
class CouchbaseClient internal constructor(private val config: CouchbaseConfig)
  : GoblinManagedObject(), CouchbaseClientMXBean {

  val bucket: Bucket
  private val bucketType: BucketType

  init {
    val servers = config.getServers()
    val cluster = CouchbaseClusterManager.INSTANCE.getCouchbaseCluster(servers)
    val transcoder = CouchbaseTranscoder(config.getSerializer(), config.getCompressionThreshold())
    bucket = cluster.openBucket(
        config.getBucketName(),
        config.mapper.bucketPassword,
        listOf(transcoder),
        config.getOpenBucketTimeoutInSeconds().toLong(),
        TimeUnit.SECONDS)

    val bucketManager = bucket.bucketManager()
    val bucketInfo = bucketManager.info()
    bucketType = bucketInfo.type()
    if (GoblinSystem.runtimeMode() === RuntimeMode.UNIT_TEST && bucketType === BucketType.COUCHBASE) {
      throw IllegalArgumentException("COUCHBASE bucket not allowed under UNIT TEST")
    }
  }

  fun flush() {
    if (getFlushable()) {
      bucket.bucketManager().flush()
      logger.debug("{Couchbase} Couchbase bucket [${config.getName()}/${config.getBucketName()}] flushed")
    }
  }

  override fun getName(): String {
    return config.getName()
  }

  override fun getFlushable(): Boolean {
    return bucketType != BucketType.COUCHBASE && config.getFlushable()
  }

  override fun getBucketType(): BucketType {
    return bucketType
  }

  override fun disposeBean() {
    val state = if (bucket.close()) "SUCCESS" else "FAILURE"
    logger.debug("{Couchbase} Couchbase bucket [${config.getName()}/${config.getBucketName()}] closed [$state]")
    val servers = config.getServers()
    CouchbaseClusterManager.INSTANCE.closeCouchbaseCluster(servers)
  }
}