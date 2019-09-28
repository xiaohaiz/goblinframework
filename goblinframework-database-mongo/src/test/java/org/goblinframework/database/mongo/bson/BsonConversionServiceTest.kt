package org.goblinframework.database.mongo.bson

import org.bson.BsonArray
import org.bson.BsonDocument
import org.bson.types.ObjectId
import org.goblinframework.core.container.SpringContainerObject
import org.goblinframework.core.util.CollectionUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import java.time.Instant

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class BsonConversionServiceTest : SpringContainerObject() {

  @Test
  fun toList() {
    val idList = mutableListOf<ObjectId>()
    idList.add(ObjectId())
    idList.add(ObjectId())
    idList.add(ObjectId())
    val bson = BsonConversionService.toBson(idList) as BsonArray
    val list = BsonConversionService.toList(bson, ObjectId::class.java)
    val delta = CollectionUtils.calculateCollectionDelta(idList, list)
    assertTrue(delta.isEmpty)
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
    map = BsonConversionService.toObject<LinkedHashMap<String, ObjectId>>(doc, jt)
    val value = map["value"] as ObjectId
    assertEquals(id, value)
  }

  @Test
  fun stringToObjectId() {
    val id = ObjectId()
    val i = LinkedHashMap<String, String>().apply { this["id"] = id.toHexString() }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val factory = BsonMapper.getDefaultTypeFactory()
    val mt = factory.constructMapType(LinkedHashMap::class.java, String::class.java, ObjectId::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, ObjectId>>(doc, mt)
    assertEquals(id, o["id"] as ObjectId)
  }

  @Test
  fun instant() {
    val now = Instant.now()
    val i = LinkedHashMap<String, Instant>().apply { this["now"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val factory = BsonMapper.getDefaultTypeFactory()
    val mt = factory.constructMapType(LinkedHashMap::class.java, String::class.java, Instant::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Instant>>(doc, mt)
    assertEquals(now, o["now"] as Instant)
  }
}