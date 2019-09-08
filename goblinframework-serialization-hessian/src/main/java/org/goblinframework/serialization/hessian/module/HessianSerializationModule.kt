package org.goblinframework.serialization.hessian.module

import org.goblinframework.core.bootstrap.GoblinChildModule

class HessianSerializationModule : GoblinChildModule {

  override fun name(): String {
    return "SERIALIZATION:HESSIAN"
  }
}