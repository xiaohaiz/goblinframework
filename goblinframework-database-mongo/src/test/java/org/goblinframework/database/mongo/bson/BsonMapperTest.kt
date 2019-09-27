package org.goblinframework.database.mongo.bson

import org.bson.types.ObjectId
import org.goblinframework.core.container.SpringManagedBean
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class BsonMapperTest : SpringManagedBean() {

  @Test
  fun objectId() {
    val mapper = BsonMapper.getDefaultObjectMapper()
    val id = ObjectId()
//    val bos = ByteArrayOutputStream()
//    mapper.writeValue(bos, id)
//    bos.toInputStream().use {
//      mapper.readValue(it, ObjectId::class.java)
//    }

    val map = mutableMapOf<String, Any>()
    map["x"] = id
    val doc = BsonMapper.toDocument(map)
    println("")
    val m = BsonMapper.fromBsonDocument(doc)
    println(m["x"]!!.javaClass.name)
    println("")
  }
}