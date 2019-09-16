package org.goblinframework.core.mapper

import org.bson.types.ObjectId
import org.goblinframework.core.container.SpringManagedBean
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import java.time.Instant

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class YamlMapperTest : SpringManagedBean() {

  @Test
  fun objectId() {
    val source = ObjectId()
    val s = YamlMapper.toJson(source)
    val target = YamlMapper.asObject(s, ObjectId::class.java)
    assertEquals(source, target)
  }

  @Test
  fun instant() {
    val source = Instant.now()
    val s = YamlMapper.toJson(source)
    val target = YamlMapper.asObject(s, Instant::class.java)
    assertEquals(source, target)
  }
}