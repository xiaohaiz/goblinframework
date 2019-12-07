package org.goblinframework.database.mysql.persistence

import org.goblinframework.dao.core.annotation.GoblinConnection
import org.springframework.stereotype.Repository

@Repository
@GoblinConnection(name = "_ut")
class StringCreateTimeEntityPersistence : GoblinStaticPersistence<StringCreateTimeEntity, Long>()