package org.goblinframework.core.event.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.XmlUtils
import java.net.URL

@Singleton
class EventBusConfigLoader private constructor() {

  companion object {
    @JvmField val INSTANCE = EventBusConfigLoader()
  }

  private val channels = linkedMapOf<String, EventBusChannelConfig>()

  init {
    val scanSequence = arrayOf(
        "META-INF/goblin/event-bus.xml",
        "config/event-bus.xml",
        "META-INF/goblin/event-bus-test.xml",
        "config/event-bus-test.xml")
    val classLoader = ClassUtils.getDefaultClassLoader()
    for (path in scanSequence) {
      classLoader.getResource(path)?.run {
        parseConfig(this)
      }
    }
  }

  fun getChannelConfigs(): List<EventBusChannelConfig> {
    return channels.values.toList()
  }

  private fun parseConfig(url: URL) {
    url.openStream().use {
      val document = XmlUtils.parseDocument(it)
      val root = document.documentElement
      XmlUtils.getSingleChildElement(root, "channels")?.run {
        XmlUtils.getChildElements(this, "channel").forEach {
          val config = EventBusChannelConfig()
          config.channel = it.getAttribute("name")
          config.ringBufferSize = it.getAttribute("ringBufferSize").toInt()
          config.workerHandlers = it.getAttribute("workerHandlers").toInt()
          channels[config.channel] = config
        }
      }
    }
  }
}