package org.goblinframework.database.mysql.persistence

import org.goblinframework.dao.core.annotation.GoblinConnection
import org.goblinframework.dao.mysql.annotation.GoblinTable
import org.springframework.stereotype.Repository

@Repository
@GoblinConnection(name = "_ut")
@GoblinTable(table = "UT_UNIQUE_ID_T")
class UniqueIdEntityPersistence : GoblinStaticPersistence<UniqueIdEntity, Long>()