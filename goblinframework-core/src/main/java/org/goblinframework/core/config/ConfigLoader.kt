package org.goblinframework.core.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.event.EventBus
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.DigestUtils
import org.goblinframework.core.util.IOUtils
import org.goblinframework.core.util.StringUtils
import org.ini4j.Ini
import org.springframework.core.io.ClassPathResource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Supplier


@Singleton
@GoblinManagedBean("CORE")
class ConfigLoader private constructor() : GoblinManagedObject(), ConfigLoaderMXBean {

  companion object {
    @JvmField val INSTANCE = ConfigLoader()
  }

  private val loadTimes = AtomicLong()
  private val resourceFiles = mutableListOf<String>()
  private val md5 = AtomicReference<String>()
  private val config = AtomicReference<Config>()
  private val applicationName = AtomicReference<String>()
  private val configMappingLoader: ConfigMappingLoader
  private val scheduler: ConfigLoaderScheduler


  init {
    internalReload()
    configMappingLoader = ConfigMappingLoader()
    scheduler = ConfigLoaderScheduler(this)
    EventBus.subscribe(scheduler)
  }

  fun getConfig(section: String, name: String): String? {
    return getConfig(section, name, null)
  }

  fun getConfig(section: String, name: String, defaultValue: Supplier<String>?): String? {
    return config.get().getSection(section)?.get(name) ?: defaultValue?.get()
  }

  @Synchronized
  fun reload(): Boolean {
    return try {
      internalReload()
    } catch (ex: Exception) {
      logger.error("Exception raised when reloading config(s)", ex)
      false
    }
  }

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

    var firstTime = false
    var needLoad = false
    if (loadTimes.get() == 1.toLong()) {
      firstTime = true
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
    if (firstTime) {
      var applicationName = System.getProperty("org.goblinframework.core.applicationName")
      if (applicationName == null) {
        applicationName = getConfig("core", "org.goblinframework.core.applicationName", Supplier { "UNKNOWN" })
      }
      this.applicationName.set(applicationName!!)
    }
    return needLoad
  }

  private fun internalLoad(dataList: List<Pair<String, ByteArray>>) {
    val config = Config()
    dataList.map { it.second }.forEach { bs ->
      val ini = Ini()
      ini.load(ByteArrayInputStream(bs))
      for (section in ini.values) {
        val sectionName = section.name
        config.createSection(sectionName).putAll(section)
      }
    }
    this.config.set(config)
  }

  fun close() {
    unregisterIfNecessary()
    configMappingLoader.close()
    EventBus.unsubscribe(scheduler)
    ConfigLocationScanner.INSTANCE.close()
  }

  override fun getApplicationName(): String {
    return applicationName.get()
  }
}