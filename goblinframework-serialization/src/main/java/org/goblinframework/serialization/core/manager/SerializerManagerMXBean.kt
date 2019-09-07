package org.goblinframework.serialization.core.manager

import java.lang.management.PlatformManagedObject

interface SerializerManagerMXBean : PlatformManagedObject {

  fun getSerializerList(): Array<SerializerMXBean>

}