package org.goblinframework.core.config

import org.goblinframework.api.spi.ConfigFileProvider
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.ServiceInstaller
import org.goblinframework.core.util.StringUtils
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import java.io.File
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean


@GoblinManagedBean("CORE")
class ConfigLocationScanner internal constructor() : GoblinManagedObject(), ConfigLocationScannerMXBean {

  private val configFile: String
  private val configPath: String
  private var configPathUrl: URL? = null
  private val foundInFilesystem = AtomicBoolean()
  private val candidatePaths = mutableListOf<File>()

  init {
    val provider = ServiceInstaller.installedFirst(ConfigFileProvider::class.java)
    configFile = provider?.configFile() ?: "goblin.ini"
    configPath = "config/$configFile"
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

  fun scan(filename: String): MutableList<Resource> {
    return candidatePaths
        .asSequence()
        .map { it.path }
        .map { File(it + File.separator + filename) }
        .filter { it.exists() && it.isFile }
        .map { FileSystemResource(it) }
        .filter { it.exists() && it.isReadable }
        .toMutableList()
  }

  internal fun close() {
    unregisterIfNecessary()
  }

  override fun getConfigFile(): String {
    return configFile
  }

  override fun getConfigPath(): String {
    return configPath
  }

  override fun getConfigPathUrl(): String? {
    return configPathUrl?.toString()
  }

  override fun getFoundInFileSystem(): Boolean {
    return foundInFilesystem.get()
  }

  override fun getCandidatePathList(): Array<String> {
    return candidatePaths.map { it.path }.toTypedArray()
  }
}