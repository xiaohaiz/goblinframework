package org.goblinframework.database.mysql.persistence

import org.goblinframework.database.core.annotation.GoblinConnection
import org.springframework.stereotype.Repository

@Repository
@GoblinConnection(name = "_ut")
class UniqueIdEntityPersistence : GoblinStaticPersistence<UniqueIdEntity, Long>()