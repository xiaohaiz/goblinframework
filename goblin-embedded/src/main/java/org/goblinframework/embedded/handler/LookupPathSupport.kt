package org.goblinframework.embedded.handler

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

abstract class LookupPathSupport protected constructor() {

  private val lock = ReentrantReadWriteLock()
  private val lookupPathMappings = mutableMapOf<String, String>()

  fun registerWelcomeFile(welcomeFile: String) {
    registerLookupPathMapping("/", welcomeFile)
  }

  fun registerLookupPathMapping(target: String,
                                mapTo: String) {
    lock.write { lookupPathMappings[target] = mapTo }
  }

  fun doTransformLookupPath(lookupPath: String): String? {
    return lock.read { lookupPathMappings[lookupPath] }
  }
}