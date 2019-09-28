package org.goblinframework.api.management

import org.goblinframework.api.annotation.Internal

@Internal(uniqueInstance = true)
interface IManagementControllerManager {

  fun register(controller: Any)

}
