package org.goblinframework.dao.mongo.bson.serializer;

import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;
import org.goblinframework.database.mongo.bson.BsonConversionService;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class BsonObjectIdSerializerTest {

  @Test
  public void serialize() {
    ObjectId objectId = new ObjectId();
    LinkedHashMap<String, ObjectId> map = new LinkedHashMap<>();
    map.put("a", objectId);
    BsonDocument document = (BsonDocument) BsonConversionService.toBson(map);
    BsonObjectId a = document.getObjectId("a");
    assertEquals(objectId, a.getValue());
  }
}