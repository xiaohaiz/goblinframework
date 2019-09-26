package org.goblinframework.management.controller

import org.goblinframework.api.core.Install
import org.goblinframework.api.core.Singleton
import org.goblinframework.api.management.IManagementControllerManager
import java.util.*

@Singleton
class ManagementControllerManager private constructor() : IManagementControllerManager {

  companion object {
    @JvmField val INSTANCE = ManagementControllerManager()
  }

  private val controllers = mutableListOf<Any>()

  override fun register(controller: Any) {
    controllers.add(controller)
  }

  fun drain(): List<Any> {
    val drained = LinkedList(controllers)
    controllers.clear()
    return drained
  }

  @Install
  class Installer : IManagementControllerManager by INSTANCE
}