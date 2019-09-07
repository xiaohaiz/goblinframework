package org.goblinframework.serialization.core.manager

import org.goblinframework.serialization.core.Serializer
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class SerializerManagerTest {

  @Test
  fun testGetSerializer() {
    val serializer = SerializerManager.INSTANCE.getSerializer(Serializer.JAVA)
    Assert.assertNotNull(serializer)
  }
}