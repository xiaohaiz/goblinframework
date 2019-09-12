package org.goblinframework.core.serialization.hessian

import org.goblinframework.core.serialization.Serializer
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.serialization.SerializerTest

class Hessian2SerializerTest : SerializerTest() {

  override fun serializer(): Serializer {
    return SerializerManager.INSTANCE.getSerializer(SerializerMode.HESSIAN2)
  }
}