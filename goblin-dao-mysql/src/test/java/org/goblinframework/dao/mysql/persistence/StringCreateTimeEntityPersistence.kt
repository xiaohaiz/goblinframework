package org.goblinframework.dao.mysql.persistence

import org.goblinframework.dao.annotation.PersistenceConnection
import org.goblinframework.dao.mysql.annotation.MysqlPersistenceTable
import org.springframework.stereotype.Repository

@Repository
@PersistenceConnection(connection = "_ut")
@MysqlPersistenceTable(table = "UT_STRING_CREATE_TIME_T")
class StringCreateTimeEntityPersistence : GoblinStaticPersistence<StringCreateTimeEntity, Long>()