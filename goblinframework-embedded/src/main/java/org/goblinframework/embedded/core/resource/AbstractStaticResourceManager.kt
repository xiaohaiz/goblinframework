package org.goblinframework.embedded.core.resource

import org.apache.commons.io.IOUtils
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.MediaTypeFactory
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

abstract class AbstractStaticResourceManager
protected constructor(
    private val buffer: StaticResourceBuffer,
    private val lookupPathPrefix: String
) : StaticResourceManager {

  private val lock = ReentrantReadWriteLock()

  override fun getLookupPathPrefix(): String {
    return lookupPathPrefix
  }

  override fun lookup(lookupPath: String): StaticResource? {
    if (buffer === NopStaticResourceBuffer.INSTANCE) {
      return createStaticResource(loadResource(lookupPath), lookupPath)
    }
    return lock.read {
      buffer.loadFromBuffer(lookupPath)
    } ?: lock.write {
      val buffered = buffer.loadFromBuffer(lookupPath)
      if (buffered != null) {
        buffered
      } else {
        val resource = createStaticResource(loadResource(lookupPath), lookupPath)
        buffer.putIntoBuffer(lookupPath, resource)
        resource
      }
    }
  }

  abstract fun loadResource(lookupPath: String): Resource?

  private fun createStaticResource(resource: Resource?,
                                   lookupPath: String): StaticResource {
    if (resource == null || !resource.exists() || !resource.isReadable) {
      return StaticResource(null, null, 0)
    }
    return resource.inputStream.use {
      val content = IOUtils.toByteArray(it)
      val mediaType = MediaTypeFactory.getMediaType(lookupPath)
          .orElse(MediaType.APPLICATION_OCTET_STREAM)
      StaticResource(content, mediaType.toString(), resource.lastModified())
    }
  }
}