package org.goblinframework.example.database.mongo.dao

import org.bson.types.ObjectId
import org.goblinframework.dao.mongo.persistence.GoblinStaticDao
import org.goblinframework.database.core.annotation.GoblinConnection
import org.goblinframework.example.database.mongo.entity.MongoExampleData
import org.springframework.stereotype.Repository

@Repository
@GoblinConnection("example")
class MongoExampleDataDao: GoblinStaticDao<MongoExampleData, ObjectId>() {

}