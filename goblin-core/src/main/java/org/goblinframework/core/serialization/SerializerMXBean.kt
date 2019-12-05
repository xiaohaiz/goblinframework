package org.goblinframework.core.serialization

import org.goblinframework.api.core.SerializerMode
import java.lang.management.PlatformManagedObject

interface SerializerMXBean : PlatformManagedObject {

  fun getUpTime(): String

  fun getMode(): SerializerMode

  fun getSerializeCount(): Long

  fun getSerializeExceptionCount(): Long

  fun getDeserializeCount(): Long

  fun getDeserializeExceptionCount(): Long

}