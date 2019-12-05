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
public class BsonDoubleDeserializerTest {

  public static class Data {
    public Double a;
    public Double b;
    public Double c;
    public Double d;
    public Double e;
    public Double f;
  }

  @Test
  public void deserialize() {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    map.put("a", 1.1D);
    map.put("b", 2.2F);
    map.put("c", 3);
    map.put("d", 4);
    map.put("e", "5.5");
    map.put("f", "I'm not double value.");
    BsonDocument document = (BsonDocument) BsonConversionService.toBson(map);
    Data data = BsonConversionService.toObject(document, Data.class);
    assertEquals(1.1, data.a, 0.01);
    assertEquals(2.2, data.b, 0.01);
    assertEquals(3.0, data.c, 0.01);
    assertEquals(4.0, data.d, 0.01);
    assertEquals(5.5, data.e, 0.01);
    assertNull(data.f);
  }
}