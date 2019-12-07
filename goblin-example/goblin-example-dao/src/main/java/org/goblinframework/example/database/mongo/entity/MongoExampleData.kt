package org.goblinframework.example.database.mongo.entity

import org.bson.types.ObjectId
import org.goblinframework.api.dao.*
import java.io.Serializable
import java.util.*

class MongoExampleData : Serializable {
    companion object {
        private const val serialVersionUID = -4568367284300289601L
    }

    @GoblinId(GoblinId.Generator.OBJECT_ID)
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