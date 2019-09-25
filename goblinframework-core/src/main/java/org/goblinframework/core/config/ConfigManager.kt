package org.goblinframework.core.config

import org.goblinframework.api.common.Install
import org.goblinframework.api.common.Singleton
import org.goblinframework.api.config.ConfigListener
import org.goblinframework.api.config.ConfigParser
import org.goblinframework.api.config.IConfigManager
import org.goblinframework.api.schedule.CronConstants
import org.goblinframework.api.schedule.CronTask
import org.goblinframework.api.schedule.ICronTaskManager
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.util.DigestUtils
import org.goblinframework.core.util.IOUtils
import org.goblinframework.core.util.StringUtils
import org.ini4j.Ini
import org.springframework.core.io.ClassPathResource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Supplier
import kotlin.concurrent.fixedRateTimer


@Singleton
@GoblinManagedBean(type = "core")
class ConfigManager private constructor()
  : GoblinManagedObject(), IConfigManager, ConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = ConfigManager()
  }

  private val configLocationScanner = ConfigLocationScanner()
  private val mappingLocationScanner = MappingLocationScanner(configLocationScanner)
  private val configParserManager = ConfigParserManager()
  private val configListenerManager = ConfigListenerManager()

  private val loadTimes = AtomicLong()
  private val resourceFiles = mutableListOf<String>()
  private val md5 = AtomicReference<String>()
  private val config = AtomicReference<ConfigSection>()
  private val mapping = AtomicReference<ConfigMapping>(ConfigMapping())
  private val applicationName = AtomicReference<String>("UNKNOWN")
  private val timer = AtomicReference<Timer>()

  init {
    configLocationScanner.getConfigLocation()?.run {
      loadConfiguration(this)

      // parse application name
      var an = System.getProperty("org.goblinframework.core.applicationName")
      if (an == null) {
        an = getConfig("core", "applicationName")
      }
      an?.run { applicationName.set(this) }
    }

    mappingLocationScanner.getMappingLocation()?.run {
      val mapping = resource.inputStream.use {
        JsonMapper.asObject(it, ConfigMapping::class.java)
      }
      this@ConfigManager.mapping.set(mapping)
    }
  }

  fun getMapping(): ConfigMapping {
    return mapping.get()
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

  fun install() {}

  override fun initializeBean() {
    configParserManager.parseConfigs()
    ICronTaskManager.instance()?.run {
      val task = object : CronTask {
        override fun name(): String {
          return "ConfigLoaderScheduler"
        }

        override fun cronExpression(): String {
          return CronConstants.MINUTE_TIMER
        }

        override fun execute() {
          executeReload()
        }
      }
      this.register(task)
    } ?: kotlin.run {
      val timer = fixedRateTimer(
          name = "ConfigLoaderScheduler",
          daemon = true,
          initialDelay = 60000,
          period = 60000) {
        executeReload()
      }
      this.timer.set(timer)
    }
  }

  override fun disposeBean() {
    ICronTaskManager.instance()?.run {
      this.unregister("ConfigLoaderScheduler")
    } ?: kotlin.run {
      timer.getAndSet(null)?.cancel()
    }
    mappingLocationScanner.dispose()
    configLocationScanner.dispose()
  }

  private fun executeReload() {
    if (reload()) {
      configListenerManager.onConfigChanged()
    }
  }

  override fun registerConfigParser(parser: ConfigParser) {
    configParserManager.register(parser)
  }

  override fun registerConfigListener(listener: ConfigListener) {
    configListenerManager.register(listener)
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

  @Install
  class Installer : IConfigManager by INSTANCE
}