package org.goblinframework.core.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.system.RuntimeMode
import org.goblinframework.core.util.DigestUtils
import org.goblinframework.core.util.IOUtils
import org.goblinframework.core.util.StringUtils
import org.goblinframework.core.util.SystemUtils
import org.ini4j.Ini
import org.springframework.core.io.ClassPathResource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Supplier

@Singleton
@GoblinManagedBean(type = "Core")
class ConfigManager private constructor() : GoblinManagedObject(), ConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = ConfigManager()
  }

  private val configLocationScanner = ConfigLocationScanner()
  private val mappingLocationScanner = MappingLocationScanner(configLocationScanner)
  private val configParserManager = ConfigParserManager()
  private val configListenerManager = ConfigListenerManager()
  private val configManagerScanner = ConfigManagerScanner(this)

  private val loadTimes = AtomicLong()
  private val resourceFiles = mutableListOf<String>()
  private val md5 = AtomicReference<String>()
  private val config = AtomicReference<ConfigSection>()
  private val mapping = AtomicReference<ConfigMapping>(ConfigMapping())
  private val applicationName = AtomicReference<String>()
  private val runtimeMode = AtomicReference<RuntimeMode>()

  init {
    configLocationScanner.getConfigLocation()?.run {
      loadConfiguration(this)

      // parse application name
      var s = getConfig("core", "applicationName", true)
      s = StringUtils.defaultIfBlank(s, "UNKNOWN")
      applicationName.set(s!!)

      // parse runtime mode
      s = getConfig("core", "runtimeMode", true)
      s = StringUtils.defaultIfBlank(s, RuntimeMode.DEVELOPMENT.name)
      val mode = if (SystemUtils.isTestRunnerFound()) {
        RuntimeMode.UNIT_TEST
      } else {
        try {
          RuntimeMode.valueOf(s!!)
        } catch (ex: Exception) {
          RuntimeMode.DEVELOPMENT
        }
      }
      runtimeMode.set(mode)
    }

    mappingLocationScanner.getMappingLocation()?.run {
      val mapping = resource.inputStream.use {
        JsonMapper.asObject(it, ConfigMapping::class.java)
      }
      this@ConfigManager.mapping.set(mapping)
    }

    configManagerScanner.initialize()
  }

  fun getMapping(): ConfigMapping {
    return mapping.get()
  }

  fun getConfig(section: String, name: String, accessSystemProperties: Boolean = true): String? {
    return getConfig(section, name, accessSystemProperties, null)
  }

  fun getConfig(section: String, name: String, accessSystemProperties: Boolean, defaultValue: Supplier<String>?): String? {
    if (accessSystemProperties) {
      System.getProperty("org.goblinframework.$section.$name")?.run { return this }
    }
    return config.get().getSection(section)?.get(name) ?: defaultValue?.get()
  }

  fun reload() {
    configLocationScanner.getConfigLocation()?.run {
      if (loadConfiguration(this)) {
        configListenerManager.onConfigChanged()
      }
    }
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

  fun install() {}

  override fun initializeBean() {
    configParserManager.parseConfigs()
  }

  override fun disposeBean() {
    configListenerManager.dispose()
    mappingLocationScanner.dispose()
    configLocationScanner.dispose()
  }

  fun registerConfigParser(parser: ConfigParser) {
    configParserManager.register(parser)
  }

  fun subscribeConfigListener(listener: ConfigListener) {
    configListenerManager.subscribe(listener)
  }

  fun unsubscribeConfigListener(listener: ConfigListener) {
    configListenerManager.unsubscribe(listener)
  }

  override fun getConfigLocationScanner(): ConfigLocationScannerMXBean {
    return configLocationScanner
  }

  override fun getMappingLocationScanner(): MappingLocationScannerMXBean {
    return mappingLocationScanner
  }

  override fun getApplicationName(): String {
    return applicationName.get()
  }

  override fun getRuntimeMode(): RuntimeMode {
    return runtimeMode.get()
  }

}