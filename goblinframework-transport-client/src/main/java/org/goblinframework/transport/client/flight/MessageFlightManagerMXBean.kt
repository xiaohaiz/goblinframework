package org.goblinframework.transport.client.flight

import java.lang.management.PlatformManagedObject

interface MessageFlightManagerMXBean : PlatformManagedObject {

  fun getActive(): Boolean

  fun getMessageFlightPartitionList(): Array<MessageFlightPartitionMXBean>

}