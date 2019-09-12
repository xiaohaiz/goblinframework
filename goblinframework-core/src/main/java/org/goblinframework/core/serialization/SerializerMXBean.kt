package org.goblinframework.core.serialization

import java.lang.management.PlatformManagedObject

interface SerializerMXBean : PlatformManagedObject {

  fun getMode(): SerializerMode

}