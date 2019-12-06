package org.goblinframework.example.database.mongo.dao

import org.bson.types.ObjectId
import org.goblinframework.dao.mongo.persistence.GoblinStaticDao
import org.goblinframework.database.core.annotation.GoblinDatabaseConnection
import org.goblinframework.example.database.mongo.entity.MongoExampleData
import org.springframework.stereotype.Repository

@Repository
@GoblinDatabaseConnection("example")
class MongoExampleDataDao: GoblinStaticDao<MongoExampleData, ObjectId>() {

}