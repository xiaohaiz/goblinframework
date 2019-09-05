package org.goblinframework.core.management

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.management.ObjectName
import kotlin.concurrent.read
import kotlin.concurrent.write

object ObjectNameGenerator {

  private const val DOMAIN = "org.goblinframework"

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<Pair<String, String>, AtomicInteger>()

  fun generate(type: String, name: String): ObjectName {
    val key = Pair(type, name)
    val counter = lock.read { buffer[key] } ?: lock.write {
      buffer.computeIfAbsent(key) { AtomicInteger() }
    }
    val next = counter.getAndIncrement()
    var nameForUse = name
    if (next > 0) {
      nameForUse += "_$next"
    }
    return ObjectName("$DOMAIN:type=$type,name=$nameForUse")
  }

}