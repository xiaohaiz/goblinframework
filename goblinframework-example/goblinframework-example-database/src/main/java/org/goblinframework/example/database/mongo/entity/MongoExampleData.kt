package org.goblinframework.example.database.mongo.entity

import org.goblinframework.api.database.*
import org.goblinframework.api.database.Collection
import java.io.Serializable
import java.time.Instant
import java.util.*

@Database("test")
@Collection("mongo_example_data")
class MongoExampleData: Serializable {
    companion object {
        private const val serialVersionUID = -4568367284300289601L
    }

    @Id(Id.Generator.OBJECT_ID)
    var id: String? = null
    var name: String? = null
    @CreateTime
    @Field("ct")
    var createTime: Date? = null
    @UpdateTime
    @Field("ut")
    var updateTime1: Date? = null
    @Revision
    var revision: Int? = null
}