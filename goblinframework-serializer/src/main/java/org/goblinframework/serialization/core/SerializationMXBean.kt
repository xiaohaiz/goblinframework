package org.goblinframework.serialization.core

import org.goblinframework.api.serialization.Serializer0
import java.lang.management.PlatformManagedObject

interface SerializationMXBean : PlatformManagedObject {

  fun getSerializer(): Serializer0

}