package org.goblinframework.core.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.StringUtils
import java.io.File
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean


@Singleton
@GoblinManagedBean("CORE")
class ConfigLocationScanner private constructor() : GoblinManagedObject(), ConfigLocationScannerMXBean {

  companion object {
    val INSTANCE = ConfigLocationScanner()
  }

  private val configPath = "config/goblin.ini"
  private val foundInFilesystem = AtomicBoolean()
  private var configPathUrl: URL? = null
  private val candidatePaths = mutableListOf<File>()

  init {
    initialize()
  }

  private fun initialize() {
    val classLoader = ClassUtils.getDefaultClassLoader()
    val url = classLoader.getResource(configPath)
    if (url == null) {
      logger.warn("No config [$configPath] found in classpath, ignore loading config(s)")
      return
    }
    configPathUrl = url

    var foundInFileSystem = false
    var file: String
    when {
      StringUtils.equals("jar", url.protocol) -> {
        file = url.file
        file = StringUtils.substringAfter(file, "file:");
        file = StringUtils.substringBeforeLast(file, "!");
      }
      StringUtils.equals("file", url.protocol) -> {
        foundInFileSystem = true
        file = url.file
      }
      else -> throw UnsupportedOperationException()
    }
    this.foundInFilesystem.set(foundInFileSystem)

    var parent: File = File(file).parentFile
    while (true) {
      candidatePaths.add(parent)
      parent = parent.parentFile ?: break
    }
  }

  override fun getConfigPath(): String {
    return configPath
  }

  override fun getAvailable(): Boolean {
    return configPathUrl != null
  }

  override fun getFoundInFileSystem(): Boolean {
    return foundInFilesystem.get()
  }

  override fun getCandidatePathList(): Array<String> {
    return candidatePaths.map { it.path }.toTypedArray()
  }
}