package org.goblinframework.core.serialization.hessian

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.serialization.Serializer
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.serialization.SerializerTest
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class Hessian2SerializerTest : SerializerTest() {

  override fun serializer(): Serializer {
    return SerializerManager.INSTANCE.getSerializer(SerializerMode.HESSIAN2)
  }
}