package org.goblinframework.serialization.core

import java.lang.management.PlatformManagedObject

interface SerializationManagerMXBean : PlatformManagedObject {

  fun getSerializationList(): Array<SerializationMXBean>

}