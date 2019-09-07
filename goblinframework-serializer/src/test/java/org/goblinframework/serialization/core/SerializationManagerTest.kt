package org.goblinframework.serialization.core

import org.goblinframework.api.serialization.Serializer
import org.junit.Assert
import org.junit.Test

class SerializationManagerTest {

  @Test
  fun testGetSerialization() {
    val serialization = SerializationManager.INSTANCE.getSerialization(Serializer.JAVA)
    Assert.assertNotNull(serialization)
  }
}