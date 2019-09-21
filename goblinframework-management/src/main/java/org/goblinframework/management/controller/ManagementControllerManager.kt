package org.goblinframework.management.controller

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.module.spi.RegisterManagementController
import java.util.*

class ManagementControllerManager private constructor() : RegisterManagementController {

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
  class Installer : RegisterManagementController by INSTANCE
}