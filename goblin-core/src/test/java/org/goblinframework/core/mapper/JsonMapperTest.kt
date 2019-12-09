package org.goblinframework.core.mapper

import org.bson.types.ObjectId
import org.goblinframework.core.container.SpringContainerObject
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import java.time.Instant

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class JsonMapperTest : SpringContainerObject() {

  @Test
  fun objectId() {
    val source = ObjectId()
    val s = JsonMapper.toJson(source)
    val target = JsonMapper.asObject(s, ObjectId::class.java)
    assertEquals(source, target)
  }

  @Test
  fun instant() {
    val source = Instant.now()
    val s = JsonMapper.toJson(source)
    val target = JsonMapper.asObject(s, Instant::class.java)
    assertEquals(source.toEpochMilli(), target.toEpochMilli())
  }
}