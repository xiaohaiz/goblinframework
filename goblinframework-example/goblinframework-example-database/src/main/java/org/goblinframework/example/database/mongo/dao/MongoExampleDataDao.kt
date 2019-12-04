package org.goblinframework.example.database.mongo.dao

import org.goblinframework.database.core.GoblinDatabaseConnection
import org.goblinframework.database.mongo.persistence.GoblinStaticDao
import org.goblinframework.example.database.mongo.entity.MongoExampleData
import org.springframework.stereotype.Repository

@Repository
@GoblinDatabaseConnection("example")
class MongoExampleDataDao: GoblinStaticDao<MongoExampleData, String>() {

}