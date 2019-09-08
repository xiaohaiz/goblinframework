package org.goblinframework.serialization.fst.module

import org.goblinframework.core.bootstrap.GoblinChildModule

class FstSerializationModule : GoblinChildModule {

  override fun name(): String {
    return "SERIALIZATION:FST"
  }
}