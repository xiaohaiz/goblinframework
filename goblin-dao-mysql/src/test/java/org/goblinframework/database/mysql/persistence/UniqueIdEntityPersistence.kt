package org.goblinframework.database.mysql.persistence

import org.goblinframework.database.core.annotation.GoblinDatabaseConnection
import org.springframework.stereotype.Repository

@Repository
@GoblinDatabaseConnection(name = "_ut")
class UniqueIdEntityPersistence : GoblinStaticPersistence<UniqueIdEntity, Long>()