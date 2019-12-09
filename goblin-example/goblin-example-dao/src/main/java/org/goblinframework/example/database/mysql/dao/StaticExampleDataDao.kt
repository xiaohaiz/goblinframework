package org.goblinframework.example.database.mysql.dao

import org.goblinframework.dao.annotation.PersistenceConnection
import org.goblinframework.dao.mysql.annotation.MysqlPersistenceTable
import org.goblinframework.dao.mysql.persistence.GoblinStaticPersistence
import org.goblinframework.dao.ql.Criteria
import org.goblinframework.dao.ql.Query
import org.goblinframework.example.database.mysql.entity.StaticExampleData
import org.springframework.stereotype.Repository

@Repository
@PersistenceConnection(connection = "example")
@MysqlPersistenceTable(table = "STATIC_EXAMPLE_DATA")
class StaticExampleDataDao : GoblinStaticPersistence<StaticExampleData, Long>() {

  fun queryByName(name: String): List<StaticExampleData> {
    val criteria = Criteria.where("name").`is`(name)
    return directQuery(Query.query(criteria))
  }
}
