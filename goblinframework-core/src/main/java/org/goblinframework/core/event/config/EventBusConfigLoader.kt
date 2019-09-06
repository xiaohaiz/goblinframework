package org.goblinframework.core.event.config

import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.YamlUtils

object EventBusConfigLoader {

  private val CONFIG_PATHS = arrayOf(
      "META-INF/goblin/goblin-event-bus.yaml",
      "META-INF/goblin/event-bus.yaml"
  )

  val configs: List<EventBusConfig> by lazy {
    val allConfigs = mutableMapOf<String, EventBusConfig>()
    CONFIG_PATHS.forEach { loadFromPath(it, allConfigs) }
    allConfigs.values.sortedBy { it.channel }.toList()
  }

  private fun loadFromPath(path: String, allConfigs: MutableMap<String, EventBusConfig>) {
    val classLoader = ClassUtils.getDefaultClassLoader()
    val resources = classLoader.getResources(path)
    while (resources.hasMoreElements()) {
      val url = resources.nextElement()
      url.openStream().use {
        YamlUtils.asList(it, EventBusConfig::class.java).forEach { config ->
          val channel = config.channel
          allConfigs[channel] = config
        }
      }
    }
  }
}