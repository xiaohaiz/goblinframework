package org.goblinframework.database.mysql.persistence

import org.goblinframework.core.container.SpringManagedBean
import org.goblinframework.database.mysql.module.test.RebuildMysqlTable
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import javax.inject.Inject


@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class UniqueIdEntityPersistenceTest : SpringManagedBean() {

  @Inject private lateinit var persistence: UniqueIdEntityPersistence

  @Test
  @RebuildMysqlTable(name = "_ut", entity = UniqueIdEntity::class)
  fun uniqueIdEntity() {
    var e = UniqueIdEntity()
    persistence.insert(e)
    var id = e.id
    var inserted = persistence.load(id)
    assertNotNull(inserted)

    e = UniqueIdEntity()
    e.id = id + 1
    persistence.insert(e)
    id = e.id
    inserted = persistence.load(id)
    assertNotNull(inserted)
  }
}