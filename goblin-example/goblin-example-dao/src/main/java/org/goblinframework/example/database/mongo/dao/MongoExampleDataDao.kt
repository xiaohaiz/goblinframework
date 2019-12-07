package org.goblinframework.example.database.mongo.dao

import org.bson.types.ObjectId
import org.goblinframework.dao.core.annotation.GoblinConnection
import org.goblinframework.dao.mongo.annotation.GoblinCollection
import org.goblinframework.dao.mongo.annotation.GoblinDatabase
import org.goblinframework.dao.mongo.persistence.GoblinStaticDao
import org.goblinframework.example.database.mongo.entity.MongoExampleData
import org.springframework.stereotype.Repository

@Repository
@GoblinConnection("example")
@GoblinDatabase("test")
@GoblinCollection("mongo_example_data")
class MongoExampleDataDao : GoblinStaticDao<MongoExampleData, ObjectId>() {

}