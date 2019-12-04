package org.goblinframework.remote.client.module.runtime

import org.goblinframework.api.core.SerializerMode
import java.lang.management.PlatformManagedObject

interface RemoteServiceMethodInformationMXBean : PlatformManagedObject {

  fun getMethodName(): String

  fun getReturnType(): String

  fun getTimeout(): Long

  fun getRetries(): Int

  fun getEncoder(): SerializerMode

  fun getAsynchronous(): Boolean

  fun getNoResponseWait(): Boolean

  fun getDispatchAll(): Boolean

  fun getIgnoreNoProvider(): Boolean

}