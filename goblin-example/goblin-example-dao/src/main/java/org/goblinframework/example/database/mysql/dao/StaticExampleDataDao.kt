package org.goblinframework.example.database.mysql.dao

import org.goblinframework.database.core.annotation.GoblinDatabaseConnection
import org.goblinframework.database.core.eql.Criteria
import org.goblinframework.database.core.eql.Query
import org.goblinframework.database.mysql.persistence.GoblinStaticPersistence
import org.goblinframework.example.database.mysql.entity.StaticExampleData
import org.springframework.stereotype.Repository

@Repository
@GoblinDatabaseConnection(name = "example")
class StaticExampleDataDao : GoblinStaticPersistence<StaticExampleData, Long>() {

  fun queryByName(name: String): List<StaticExampleData> {
    val criteria = Criteria.where("name").`is`(name)
    return directQuery(Query.query(criteria))
  }
}
