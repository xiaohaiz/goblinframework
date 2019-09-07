package org.goblinframework.serializer.hessian.module

import org.goblinframework.core.bootstrap.GoblinChildModule

class HessianSerializerModule : GoblinChildModule {

  override fun parent(): String {
    return "SERIALIZER"
  }

  override fun name(): String {
    return "HESSIAN"
  }
}