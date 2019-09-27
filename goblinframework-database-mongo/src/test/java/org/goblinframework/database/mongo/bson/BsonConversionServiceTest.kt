package org.goblinframework.database.mongo.bson

import org.bson.BsonArray
import org.bson.BsonDocument
import org.bson.types.ObjectId
import org.goblinframework.core.container.SpringManagedBean
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class BsonConversionServiceTest : SpringManagedBean() {

  @Test
  fun toList() {
    val idList = mutableListOf<ObjectId>()
    idList.add(ObjectId())
    idList.add(ObjectId())
    idList.add(ObjectId())
    val bson = BsonConversionService.toBson(idList) as BsonArray
    val list = BsonConversionService.toList(bson, ObjectId::class.java)
    println(list)
  }

  @Test
  fun objectId() {
    val id = ObjectId()
    var map = LinkedHashMap<String, ObjectId>().apply {
      this["value"] = id
    }
    val doc = BsonConversionService.toBson(map) as BsonDocument
    val factory = BsonMapper.getDefaultObjectMapper().typeFactory
    val jt = factory.constructMapType(LinkedHashMap::class.java, String::class.java, ObjectId::class.java)
    map = BsonConversionService.convert<LinkedHashMap<String, ObjectId>>(doc, jt)
    val value = map["value"] as ObjectId
    assertEquals(id, value)
  }
}