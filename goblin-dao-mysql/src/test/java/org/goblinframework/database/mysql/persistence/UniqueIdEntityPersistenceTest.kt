package org.goblinframework.database.mysql.persistence

import org.goblinframework.core.container.SpringContainerObject
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import javax.inject.Inject


@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class UniqueIdEntityPersistenceTest : SpringContainerObject() {

  @Inject private lateinit var persistence: UniqueIdEntityPersistence

  @Test
  fun uniqueIdEntity() {
    var e = UniqueIdEntity()
    persistence.insert(e)
    var id = e.id
    var inserted = persistence.load(id!!)
    assertNotNull(inserted)

    e = UniqueIdEntity()
    e.id = id.plus(1)
    persistence.insert(e)
    id = e.id
    inserted = persistence.load(id!!)
    assertNotNull(inserted)
  }
}