package org.goblinframework.dao.mongo.bson.deserializer;

import org.bson.BsonDocument;
import org.goblinframework.database.mongo.bson.BsonConversionService;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class BsonLongDeserializerTest {

  public static class Data {
    public Long a, b, c, d, e, f;
  }

  @Test
  public void deserialize() {
    Date date = new Date();
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    map.put("a", 1L);
    map.put("b", 2);
    map.put("c", 3D);
    map.put("d", "4");
    map.put("e", date);
    map.put("f", "I'm not long value.");
    BsonDocument document = (BsonDocument) BsonConversionService.toBson(map);
    Data data = BsonConversionService.toObject(document, Data.class);
    assertEquals(1, data.a.longValue());
    assertEquals(2, data.b.longValue());
    assertEquals(3, data.c.longValue());
    assertEquals(4, data.d.longValue());
    assertEquals(date.getTime(), data.e.longValue());
    assertNull(data.f);
  }
}