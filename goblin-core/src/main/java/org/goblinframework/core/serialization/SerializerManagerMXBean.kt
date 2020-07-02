package org.goblinframework.core.serialization

import java.lang.management.PlatformManagedObject

interface SerializerManagerMXBean : PlatformManagedObject {

  fun getSerializerList(): Array<SerializerMXBean>

}