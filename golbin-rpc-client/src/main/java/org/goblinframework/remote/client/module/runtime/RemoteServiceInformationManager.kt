package org.goblinframework.remote.client.module.runtime

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean("RemoteClient")
class RemoteServiceInformationManager private constructor()
  : GoblinManagedObject(), RemoteServiceInformationManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServiceInformationManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = IdentityHashMap<Class<*>, RemoteServiceInformation>()

  fun getRemoteServiceInformation(interfaceClass: Class<*>): RemoteServiceInformation {
    if (!interfaceClass.isInterface) {
      throw UnsupportedOperationException("Interface class is required")
    }
    return lock.read { buffer[interfaceClass] } ?: lock.write {
      buffer[interfaceClass] ?: kotlin.run {
        val information = RemoteServiceInformationParser.parse(interfaceClass)
        buffer[interfaceClass] = information
        information
      }
    }
  }

  override fun getRemoteServiceInformationList(): Array<RemoteServiceInformationMXBean> {
    return lock.read { buffer.values.toTypedArray() }
  }

  override fun disposeBean() {
    lock.write {
      buffer.values.forEach { it.dispose() }
      buffer.clear()
    }
  }

}