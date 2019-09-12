package org.goblinframework.core.serialization.fst

import org.goblinframework.core.serialization.Serializer
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.serialization.SerializerTest

class FstSerializerTest : SerializerTest() {

  override fun serializer(): Serializer {
    return SerializerManager.INSTANCE.getSerializer(SerializerMode.FST)
  }
}