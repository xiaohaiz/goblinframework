package org.goblinframework.remote.client.module.runtime

import org.goblinframework.api.core.SerializerMode
import java.lang.management.PlatformManagedObject

interface RemoteServiceInformationMXBean : PlatformManagedObject {

  fun getServiceInterface(): String

  fun getServiceVersion(): String

  fun getServiceTimeout(): Long

  fun getServiceRetries(): Int

  fun getServiceEncoder(): SerializerMode

  fun getRemoteServiceMethodInformationList(): Array<RemoteServiceMethodInformationMXBean>

}