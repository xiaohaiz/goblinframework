package org.goblinframework.dao.mongo.bson.deserializer;

import org.bson.BsonDocument;
import org.goblinframework.database.mongo.bson.BsonConversionService;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class BsonIntegerDeserializerTest {

  public static class Data {
    public Integer a, b, c, d, e;
  }

  @Test
  public void deserialize() {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2L);
    map.put("c", 3D);
    map.put("d", "4");
    map.put("e", "I'm not integer value.");
    BsonDocument document = (BsonDocument) BsonConversionService.toBson(map);
    Data data = BsonConversionService.toObject(document, Data.class);
    assertEquals(1, data.a.intValue());
    assertEquals(2, data.b.intValue());
    assertEquals(3, data.c.intValue());
    assertEquals(4, data.d.intValue());
    assertNull(data.e);
  }
}