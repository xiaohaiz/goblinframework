package org.goblinframework.core.config

import org.goblinframework.api.common.Install
import org.goblinframework.api.common.Singleton
import org.goblinframework.api.config.ConfigParser
import org.goblinframework.api.config.IConfigManager

@Singleton
class ConfigManager private constructor() : IConfigManager {

  companion object {
    @JvmField val INSTANCE = ConfigManager()
  }

  private val parsers = mutableListOf<ConfigParser>()

  @Synchronized
  override fun registerConfigParser(parser: ConfigParser) {
    parsers.add(parser)
  }

  fun asList(): List<ConfigParser> {
    return parsers.toMutableList()
  }

  @Install
  class Installer : IConfigManager by INSTANCE
}