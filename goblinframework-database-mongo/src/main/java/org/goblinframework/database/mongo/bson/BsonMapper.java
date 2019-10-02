package org.goblinframework.database.mongo.bson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import de.undercouch.bson4jackson.BsonParser;
import de.undercouch.bson4jackson.deserializers.BsonDeserializers;
import de.undercouch.bson4jackson.serializers.BsonSerializers;
import org.bson.types.ObjectId;
import org.goblinframework.database.mongo.bson.deserializer.BsonDateDeserializer;
import org.goblinframework.database.mongo.bson.deserializer.BsonInstantDeserializer;
import org.goblinframework.database.mongo.bson.deserializer.BsonObjectIdDeserializer;
import org.goblinframework.database.mongo.bson.introspect.BsonIntrospector;
import org.goblinframework.database.mongo.bson.serializer.BsonInstantSerializer;
import org.goblinframework.database.mongo.bson.serializer.BsonObjectIdSerializer;

import java.time.Instant;
import java.util.Date;

abstract public class BsonMapper {

  private static final ObjectMapper DEFAULT_OBJECT_MAPPER;

  static {
    DEFAULT_OBJECT_MAPPER = createObjectMapper();
    DEFAULT_OBJECT_MAPPER.setAnnotationIntrospector(new BsonIntrospector());
  }

  private static ObjectMapper createObjectMapper() {
    GoblinBsonFactory factory = new GoblinBsonFactory();
    factory.enable(BsonParser.Feature.HONOR_DOCUMENT_LENGTH);
    ObjectMapper mapper = new ObjectMapper(factory);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.registerModule(new SimpleModule() {
      @Override
      public void setupModule(SetupContext context) {
        super.setupModule(context);

        BsonSerializers serializers = new BsonSerializers();
        serializers.addSerializer(Instant.class, new BsonInstantSerializer());
        serializers.addSerializer(ObjectId.class, new BsonObjectIdSerializer());
        context.addSerializers(serializers);

        BsonDeserializers deserializers = new BsonDeserializers();
        deserializers.addDeserializer(Date.class, new BsonDateDeserializer());
        deserializers.addDeserializer(Instant.class, new BsonInstantDeserializer());
        deserializers.addDeserializer(ObjectId.class, new BsonObjectIdDeserializer());
        context.addDeserializers(deserializers);
      }
    });
    return mapper;
  }

  public static ObjectMapper getDefaultObjectMapper() {
    return DEFAULT_OBJECT_MAPPER;
  }

  public static TypeFactory getDefaultTypeFactory() {
    return getDefaultObjectMapper().getTypeFactory();
  }
}
