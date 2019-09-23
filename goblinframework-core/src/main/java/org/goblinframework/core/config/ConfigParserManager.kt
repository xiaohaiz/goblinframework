package org.goblinframework.core.config

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.config.ConfigParser
import org.goblinframework.api.config.IConfigParserManager

@Singleton
class ConfigParserManager private constructor() : IConfigParserManager {

  companion object {
    @JvmField val INSTANCE = ConfigParserManager()
  }

  private val parsers = mutableListOf<ConfigParser>()

  @Synchronized
  override fun register(parser: ConfigParser) {
    parsers.add(parser)
  }

  fun asList(): List<ConfigParser> {
    return parsers.toMutableList()
  }

  @Install
  class Installer : IConfigParserManager by INSTANCE
}