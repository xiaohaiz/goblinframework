package org.goblinframework.transport.client.flight

import java.lang.management.PlatformManagedObject

interface MessageFlightPartitionMXBean : PlatformManagedObject {

  fun getId(): Int

  fun getSize(): Int

  fun getAttachCount(): Long

  fun getDetachCount(): Long

  fun getExpireCount(): Long

  fun getLastActiveTimestamp(): String?
}