package org.goblinframework.serialization.core

import org.goblinframework.api.serialization.Serializer0
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class SerializationManagerTest {

  @Test
  fun testGetSerialization() {
    val serialization = SerializationManager.INSTANCE.getSerialization(Serializer0.JAVA)
    Assert.assertNotNull(serialization)
  }
}