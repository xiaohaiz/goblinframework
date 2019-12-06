package org.goblinframework.example.database.mongo.entity

import org.bson.types.ObjectId
import org.goblinframework.api.dao.*
import org.goblinframework.dao.mongo.annotation.GoblinCollection
import org.goblinframework.dao.mongo.annotation.GoblinDatabase
import java.io.Serializable
import java.util.*

@GoblinDatabase("test")
@GoblinCollection("mongo_example_data")
class MongoExampleData: Serializable {
    companion object {
        private const val serialVersionUID = -4568367284300289601L
    }

    @Id(Id.Generator.OBJECT_ID)
    var id: ObjectId? = null
    var name: String? = null
    @CreateTime
    @Field("ct")
    var createTime: Date? = null
    @UpdateTime
    @Field("ut")
    var updateTime: Date? = null
    @Revision
    var revision: Int? = null
}