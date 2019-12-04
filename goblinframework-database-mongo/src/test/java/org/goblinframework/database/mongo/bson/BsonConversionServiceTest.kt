package org.goblinframework.database.mongo.bson

import org.bson.BsonArray
import org.bson.BsonDocument
import org.bson.types.ObjectId
import org.goblinframework.core.container.SpringContainerObject
import org.goblinframework.core.util.CollectionUtils
import org.goblinframework.core.util.DateFormatUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import java.time.Instant
import java.util.*
import kotlin.collections.LinkedHashMap

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

  // ======================================================
  // java.util.Calendar
  // ======================================================

  @Test
  fun calendarToCalendar() {
    val now = Calendar.getInstance()
    val i = LinkedHashMap<String, Calendar>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Calendar::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Calendar>>(doc, mt)
    assertEquals(now.timeInMillis, (o["value"] as Calendar).timeInMillis)
  }

  @Test
  fun dateToCalendar() {
    val now = Date()
    val i = LinkedHashMap<String, Date>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Calendar::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Calendar>>(doc, mt)
    assertEquals(now.time, (o["value"] as Calendar).timeInMillis)
  }

  @Test
  fun instantToCalendar() {
    val now = Instant.now()
    val i = LinkedHashMap<String, Instant>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Calendar::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Calendar>>(doc, mt)
    assertEquals(now.toEpochMilli(), (o["value"] as Calendar).timeInMillis)
  }

  @Test
  fun longToCalendar() {
    val now = System.currentTimeMillis()
    val i = LinkedHashMap<String, Long>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Calendar::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Calendar>>(doc, mt)
    assertEquals(now, (o["value"] as Calendar).timeInMillis)
  }

  @Test
  fun stringToCalendar() {
    val now = DateFormatUtils.format(Date())!!
    val i = LinkedHashMap<String, String>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Calendar::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Calendar>>(doc, mt)
    assertEquals(now, DateFormatUtils.format(o["value"] as Calendar))
  }

  // ======================================================
  // java.util.Date
  // ======================================================

  @Test
  fun calendarToDate() {
    val now = Calendar.getInstance()
    val i = LinkedHashMap<String, Calendar>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Date::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Date>>(doc, mt)
    assertEquals(now.time, o["value"] as Date)
  }

  @Test
  fun dateToDate() {
    val now = Date()
    val i = LinkedHashMap<String, Date>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Date::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Date>>(doc, mt)
    assertEquals(now, o["value"] as Date)
  }

  @Test
  fun instantToDate() {
    val now = Instant.now()
    val i = LinkedHashMap<String, Instant>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Date::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Date>>(doc, mt)
    assertEquals(Date.from(now), o["value"] as Date)
  }

  @Test
  fun longToDate() {
    val now = System.currentTimeMillis()
    val i = LinkedHashMap<String, Long>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Date::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Date>>(doc, mt)
    assertEquals(now, (o["value"] as Date).time)
  }

  @Test
  fun stringToDate() {
    val now = DateFormatUtils.format(Date())!!
    val i = LinkedHashMap<String, String>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Date::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Date>>(doc, mt)
    assertEquals(now, DateFormatUtils.format(o["value"] as Date))
  }

  // ======================================================
  // java.time.Instant
  // ======================================================

  @Test
  fun calendarToInstant() {
    val now = Calendar.getInstance()
    val i = LinkedHashMap<String, Calendar>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Instant::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Instant>>(doc, mt)
    assertEquals(now.timeInMillis, (o["value"] as Instant).toEpochMilli())
  }

  @Test
  fun dateToInstant() {
    val now = Date()
    val i = LinkedHashMap<String, Date>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Instant::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Instant>>(doc, mt)
    assertEquals(now.time, (o["value"] as Instant).toEpochMilli())
  }

  @Test
  fun instantToInstant() {
    val now = Instant.now()
    val i = LinkedHashMap<String, Instant>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Instant::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Instant>>(doc, mt)
    assertEquals(now, o["value"] as Instant)
  }

  @Test
  fun longToInstant() {
    val now = System.currentTimeMillis()
    val i = LinkedHashMap<String, Long>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Instant::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Instant>>(doc, mt)
    assertEquals(now, (o["value"] as Instant).toEpochMilli())
  }

  @Test
  fun stringToInstant() {
    val now = DateFormatUtils.format(Date())!!
    val i = LinkedHashMap<String, String>().apply { this["value"] = now }
    val doc = BsonConversionService.toBson(i) as BsonDocument
    val mt = BsonMapper.constructMapType(String::class.java, Instant::class.java)
    val o = BsonConversionService.toObject<LinkedHashMap<String, Instant>>(doc, mt)
    assertEquals(now, DateFormatUtils.format(o["value"] as Instant))
  }
}