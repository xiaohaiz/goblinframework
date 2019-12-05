package org.goblinframework.database.mongo.bson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import de.undercouch.bson4jackson.BsonParser;
import de.undercouch.bson4jackson.deserializers.BsonDeserializers;
import de.undercouch.bson4jackson.serializers.BsonSerializers;
import org.bson.types.ObjectId;
import org.goblinframework.dao.mongo.bson.deserializer.*;
import org.goblinframework.dao.mongo.bson.introspect.GoblinBsonIntrospector;
import org.goblinframework.database.mongo.bson.deserializer.*;
import org.goblinframework.database.mongo.bson.serializer.BsonInstantSerializer;
import org.goblinframework.database.mongo.bson.serializer.BsonObjectIdSerializer;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

abstract public class BsonMapper {

  private static final ObjectMapper DEFAULT_OBJECT_MAPPER;

  static {
    DEFAULT_OBJECT_MAPPER = createObjectMapper();
    DEFAULT_OBJECT_MAPPER.setAnnotationIntrospector(new GoblinBsonIntrospector());
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
        deserializers.addDeserializer(Boolean.class, new BsonBooleanDeserializer());
        deserializers.addDeserializer(Calendar.class, new BsonCalendarDeserializer());
        deserializers.addDeserializer(Date.class, new BsonDateDeserializer());
        deserializers.addDeserializer(Double.class, new BsonDoubleDeserializer());
        deserializers.addDeserializer(Float.class, new BsonFloatDeserializer());
        deserializers.addDeserializer(Instant.class, new BsonInstantDeserializer());
        deserializers.addDeserializer(Integer.class, new BsonIntegerDeserializer());
        deserializers.addDeserializer(Long.class, new BsonLongDeserializer());
        deserializers.addDeserializer(ObjectId.class, new BsonObjectIdDeserializer());
        deserializers.addDeserializer(String.class, new BsonStringDeserializer());
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

  public static MapType constructMapType(@NotNull Class<?> keyClass, @NotNull Class<?> valueClass) {
    TypeFactory factory = getDefaultTypeFactory();
    return factory.constructMapType(LinkedHashMap.class, keyClass, valueClass);
  }
}
