package org.goblinframework.embedded.core.handler

import org.apache.commons.io.IOUtils
import org.goblinframework.embedded.core.resource.StaticResourceManager
import org.springframework.core.io.Resource
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import javax.servlet.http.HttpServletResponse

abstract class StaticResourceSupport protected constructor()
  : LookupPathSupport() {

  private val buffer = HashMap<String, StaticResourceManager>()
  private val prefixLenSet = TreeSet<Int>()
  private val faviconContent = AtomicReference<ByteArray>()

  fun registerStaticResourceManager(staticResourceManager: StaticResourceManager) {
    Objects.requireNonNull(staticResourceManager)
    val targetPrefix = staticResourceManager.getLookupPathPrefix()
    buffer[targetPrefix] = staticResourceManager
    val len = targetPrefix.length
    prefixLenSet.add(len)
  }

  fun getStaticResourceManager(target: String): StaticResourceManager? {
    val set = prefixLenSet.headSet(target.length, true)
    if (set.isEmpty()) {
      return null
    }
    for (len in set) {
      val key = target.substring(0, len!!)
      val staticResourceManager = buffer[key]
      if (staticResourceManager != null) {
        return staticResourceManager
      }
    }
    return null
  }

  fun registerFaviconResource(favicon: Resource) {
    if (!favicon.exists() || !favicon.isReadable) {
      return
    }
    favicon.inputStream.use {
      val bytes = IOUtils.toByteArray(it)
      faviconContent.set(bytes)
    }
  }

  fun writeFaviconResponse(response: HttpServletResponse) {
    val bytes = faviconContent.get()
    if (bytes == null) {
      response.status = HttpServletResponse.SC_NOT_FOUND
    } else {
      response.status = HttpServletResponse.SC_OK
      response.contentType = "image/x-icon"
      response.setContentLength(bytes.size)
      response.outputStream.write(bytes)
    }
  }
}
