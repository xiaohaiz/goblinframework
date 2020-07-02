package org.goblinframework.core.config

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.ClassUtils
import org.goblinframework.core.util.StringUtils
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean(type = "core")
class ConfigLocationScanner internal constructor()
  : GoblinManagedObject(), ConfigLocationScannerMXBean {

  companion object {
    private val SCAN_SEQUENCE = listOf(
        "config/goblin-test.ini",
        "META-INF/goblin/goblin-test.ini",
        "config/goblin.ini",
        "META-INF/goblin/goblin.ini")
  }

  private val configLocation = AtomicReference<ConfigLocation>()
  private val foundInFilesystem = AtomicBoolean()
  private val candidatePaths = mutableListOf<File>()

  init {
    val classLoader = ClassUtils.getDefaultClassLoader()
    for (path in SCAN_SEQUENCE) {
      val url = classLoader.getResource(path)
      if (url != null) {
        configLocation.set(ConfigLocation(path, url))
        break
      }
    }
    configLocation.get()?.run { initialize(this) }
  }

  private fun initialize(location: ConfigLocation) {
    val url = location.url
    var foundInFileSystem = false
    var file: String
    when {
      StringUtils.equals("jar", url.protocol) -> {
        file = url.file
        file = StringUtils.substringAfter(file, "file:")
        file = StringUtils.substringBeforeLast(file, "!")
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

  internal fun getConfigLocation(): ConfigLocation? {
    return configLocation.get()
  }

  override fun getConfigPath(): String {
    return configLocation.get()?.url.toString()
  }

  override fun getFoundInFileSystem(): Boolean {
    return foundInFilesystem.get()
  }

  override fun getCandidatePathList(): Array<String> {
    return candidatePaths.map { it.path }.toTypedArray()
  }
}