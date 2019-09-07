package org.goblinframework.serialization.core.manager

import java.lang.management.PlatformManagedObject

interface SerializerMXBean : PlatformManagedObject {

  fun getId(): Byte

}