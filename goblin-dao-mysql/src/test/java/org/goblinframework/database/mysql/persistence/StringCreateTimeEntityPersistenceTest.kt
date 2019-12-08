package org.goblinframework.database.mysql.persistence

import org.goblinframework.core.container.SpringContainerObject
import org.goblinframework.core.util.DateFormatUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import javax.inject.Inject


@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class StringCreateTimeEntityPersistenceTest : SpringContainerObject() {

  @Inject private lateinit var persistence: StringCreateTimeEntityPersistence

  @Test
  fun stringCreateTimeEntity() {
    val e = StringCreateTimeEntity()
    persistence.insert(e)
    val id = e.id
    val inserted = persistence.load(id!!)
    assertNotNull(inserted)
    assertNotNull(inserted?.createTime)
    val createTime = DateFormatUtils.parse(inserted?.createTime)
    assertNotNull(createTime)
  }
}