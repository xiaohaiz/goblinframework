package org.goblinframework.webmvc.handler

import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.webmvc.setting.RequestHandlerSetting
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean("WEBMVC")
class RequestHandlerManagerBuilder private constructor()
  : GoblinManagedObject(), RequestHandlerManagerBuilderMXBean {

  companion object {
    val INSTANCE = RequestHandlerManagerBuilder()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<String, RequestHandlerManager>()

  fun createRequestHandlerManager(setting: RequestHandlerSetting): RequestHandlerManager {
    val name = setting.name()
    lock.write {
      buffer[name]?.run {
        throw GoblinDuplicateException("RequestHandlerManager [$name] already created")
      }
      val manager = RequestHandlerManager(setting)
      buffer[name] = manager
      return manager
    }
  }

  override fun disposeBean() {
    lock.write {
      buffer.values.forEach { it.dispose() }
      buffer.clear()
    }
  }

  override fun getRequestHandlerManagerList(): Array<RequestHandlerManagerMXBean> {
    return lock.read { buffer.values.toTypedArray() }
  }
}