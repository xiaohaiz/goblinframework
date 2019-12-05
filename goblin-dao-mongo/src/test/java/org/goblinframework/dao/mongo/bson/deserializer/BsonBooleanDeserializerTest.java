package org.goblinframework.dao.mongo.bson.deserializer;

import org.bson.BsonDocument;
import org.goblinframework.database.mongo.bson.BsonConversionService;
import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.util.LinkedHashMap;

import static org.junit.Assert.*;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class BsonBooleanDeserializerTest {

  public static class Data {
    public Boolean a;
    public Boolean b;
    public Boolean c;
    public Boolean d;
    public Boolean e;
  }

  @Test
  public void deserialize() {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    map.put("a", true);
    map.put("b", false);
    map.put("c", "true");
    map.put("d", "false");
    BsonDocument document = (BsonDocument) BsonConversionService.toBson(map);
    Data data = BsonConversionService.toObject(document, Data.class);
    assertTrue(data.a);
    assertFalse(data.b);
    assertTrue(data.c);
    assertFalse(data.d);
    assertNull(data.e);
  }
}