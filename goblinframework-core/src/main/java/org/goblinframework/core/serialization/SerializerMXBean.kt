package org.goblinframework.core.serialization

import java.lang.management.PlatformManagedObject

interface SerializerMXBean : PlatformManagedObject {

  fun getUpTime(): String

  fun getMode(): SerializerMode

  fun getSerializeCount(): Long

  fun getSerializeExceptionCount(): Long

  fun getDeserializeCount(): Long

  fun getDeserializeExceptionCount(): Long

}