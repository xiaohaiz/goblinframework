package org.goblinframework.dao.mysql.persistence

import org.goblinframework.dao.annotation.PersistenceConnection
import org.goblinframework.dao.mysql.annotation.GoblinTable
import org.springframework.stereotype.Repository

@Repository
@PersistenceConnection(connection = "_ut")
@GoblinTable(table = "UT_STRING_CREATE_TIME_T")
class StringCreateTimeEntityPersistence : GoblinStaticPersistence<StringCreateTimeEntity, Long>()