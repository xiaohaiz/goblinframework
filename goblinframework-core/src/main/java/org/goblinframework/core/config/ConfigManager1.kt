package org.goblinframework.core.config

import org.goblinframework.api.common.Install
import org.goblinframework.api.common.Singleton
import org.goblinframework.api.config.ConfigListener
import org.goblinframework.api.config.ConfigParser
import org.goblinframework.api.config.IConfigManager
import java.util.*

@Deprecated("TBR")
@Singleton
class ConfigManager1 private constructor() : IConfigManager {

  companion object {
    @JvmField val INSTANCE = ConfigManager1()
  }

  private val parsers = Collections.synchronizedList(mutableListOf<ConfigParser>())
  private val listeners = Collections.synchronizedList(mutableListOf<ConfigListener>())

  override fun registerConfigParser(parser: ConfigParser) {
    parsers.add(parser)
  }

  override fun registerConfigListener(listener: ConfigListener) {
    listeners.add(listener)
  }

  fun getConfigParsers(): List<ConfigParser> {
    return parsers.toMutableList()
  }

  fun getConfigListeners(): List<ConfigListener> {
    return listeners.toMutableList()
  }

  @Install
  class Installer : IConfigManager by INSTANCE
}