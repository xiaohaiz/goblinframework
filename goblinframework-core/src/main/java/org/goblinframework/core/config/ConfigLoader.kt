package org.goblinframework.core.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.DigestUtils
import org.goblinframework.core.util.IOUtils
import org.goblinframework.core.util.StringUtils
import org.springframework.core.io.ClassPathResource
import java.io.ByteArrayOutputStream
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

@Singleton
@GoblinManagedBean("CORE")
class ConfigLoader private constructor() : GoblinManagedObject(), ConfigLoaderMXBean {

  companion object {
    @JvmField val INSTANCE = ConfigLoader()
  }

  private val loadTimes = AtomicLong()
  private val resourceFiles = mutableListOf<String>()
  private val md5 = AtomicReference<String>()

  init {
    internalReload()
  }

  @Synchronized
  private fun internalReload(): Boolean {
    loadTimes.incrementAndGet()

    val scanner = ConfigLocationScanner.INSTANCE
    if (scanner.getConfigPathUrl() == null) {
      return false
    }
    val configFile = scanner.getConfigFile()
    val resources = scanner.scan(configFile)
    if (!scanner.getFoundInFileSystem()) {
      resources.add(0, ClassPathResource(scanner.getConfigPath()))
    }

    val dataList = resources.map {
      val url = it.url.toString()
      val data = it.inputStream.use { s -> IOUtils.toByteArray(s) }
      Pair(url, data)
    }.toList()

    resourceFiles.clear()
    dataList.forEach { resourceFiles.add(it.first) }

    val md5 = DigestUtils.md5Hex(ByteArrayOutputStream(512).use { s ->
      dataList.forEach { s.write(it.second) }
      s.toByteArray()
    })

    var needLoad = false
    if (loadTimes.get() == 1.toLong()) {
      needLoad = true
    } else {
      val previous = this.md5.get()
      if (!StringUtils.equals(md5, previous)) {
        logger.info("Config modification detected, trigger reload.")
        needLoad = true
      }
    }
    if (needLoad) {
      internalLoad(dataList)
      this.md5.set(md5)
    }
    return needLoad
  }

  private fun internalLoad(dataList: List<Pair<String, ByteArray>>) {

  }
}