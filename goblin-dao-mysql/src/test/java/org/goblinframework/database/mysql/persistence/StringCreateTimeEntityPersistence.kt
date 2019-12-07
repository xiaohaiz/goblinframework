package org.goblinframework.database.mysql.persistence

import org.goblinframework.dao.core.annotation.GoblinConnection
import org.goblinframework.dao.mysql.annotation.GoblinTable
import org.springframework.stereotype.Repository

@Repository
@GoblinConnection(name = "_ut")
@GoblinTable(table = "UT_STRING_CREATE_TIME_T")
class StringCreateTimeEntityPersistence : GoblinStaticPersistence<StringCreateTimeEntity, Long>()