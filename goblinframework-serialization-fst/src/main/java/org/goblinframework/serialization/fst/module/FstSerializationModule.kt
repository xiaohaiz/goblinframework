package org.goblinframework.serialization.fst.module

import org.goblinframework.core.bootstrap.GoblinChildModule

class FstSerializationModule : GoblinChildModule {

  override fun parent(): String {
    return "SERIALIZATION"
  }

  override fun name(): String {
    return "FST"
  }
}