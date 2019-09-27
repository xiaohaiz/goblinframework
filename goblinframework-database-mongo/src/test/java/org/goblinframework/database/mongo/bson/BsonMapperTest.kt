package org.goblinframework.database.mongo.bson

import org.goblinframework.core.container.SpringManagedBean
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class BsonMapperTest : SpringManagedBean() {

  @Test
  fun mapper() {
    assertNotNull(BsonMapper.getDefaultObjectMapper())
  }
}