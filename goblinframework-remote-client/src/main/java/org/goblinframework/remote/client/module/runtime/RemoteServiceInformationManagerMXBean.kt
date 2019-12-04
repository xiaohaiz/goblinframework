package org.goblinframework.remote.client.module.runtime

import java.lang.management.PlatformManagedObject

interface RemoteServiceInformationManagerMXBean : PlatformManagedObject {

  fun getRemoteServiceInformationList(): Array<RemoteServiceInformationMXBean>

}