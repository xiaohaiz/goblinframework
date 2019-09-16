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

  private val configLocationScanner = ConfigLocationScanner()
  private val configMappingLoader = ConfigMappingLoader(configLocationScanner)

  private val loadTimes = AtomicLong()
  private val resourceFiles = mutableListOf<String>()
  private val md5 = AtomicReference<String>()
  private val config = AtomicReference<ConfigSection>()
  private val applicationName = AtomicReference<String>("UNKNOWN")
  private val scheduler: ConfigLoaderScheduler

  init {
    configLocationScanner.getConfigLocation()?.run {
      loadConfiguration(this)

      // parse application name
      var an = System.getProperty("org.goblinframework.core.applicationName")
      if (an == null) {
        an = getConfig("core", "org.goblinframework.core.applicationName")
      }
      an?.run { applicationName.set(this) }

      // parse config mapping
      var cmf = System.getProperty("org.goblinframework.core.configMappingFile")
      if (cmf == null) {
        cmf = getConfig("core", "org.goblinframework.core.configMappingFile", Supplier { "goblin.json" })
      }
      configMappingLoader.initialize(cmf!!)
    }
    scheduler = ConfigLoaderScheduler(this)
    EventBus.subscribe(scheduler)
  }

  fun getMapping(): ConfigMapping {
    return configMappingLoader.mapping.get()
  }

  fun getConfig(section: String, name: String): String? {
    return getConfig(section, name, null)
  }

  fun getConfig(section: String, name: String, defaultValue: Supplier<String>?): String? {
    return config.get().getSection(section)?.get(name) ?: defaultValue?.get()
  }

  @Synchronized
  fun reload(): Boolean {
    return configLocationScanner.getConfigLocation()?.run { loadConfiguration(this) } ?: false
  }

  private fun loadConfiguration(location: ConfigLocation): Boolean {
    loadTimes.incrementAndGet()
    val resources = configLocationScanner.scan(location.filename())
    if (!configLocationScanner.getFoundInFileSystem()) {
      resources.add(0, ClassPathResource(location.path))
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
      loadConfiguration(dataList)
      this.md5.set(md5)
    }
    return needLoad
  }

  private fun loadConfiguration(dataList: List<Pair<String, ByteArray>>) {
    val config = ConfigSection()
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
    EventBus.unsubscribe(scheduler)
    configMappingLoader.close()
    configLocationScanner.close()
  }

  override fun getApplicationName(): String {
    return applicationName.get()
  }
}