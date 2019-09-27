package org.goblinframework.database.mongo.bson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.undercouch.bson4jackson.BsonParser;
import de.undercouch.bson4jackson.deserializers.BsonDeserializers;
import de.undercouch.bson4jackson.serializers.BsonSerializers;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bson.*;
import org.bson.io.BasicOutputBuffer;
import org.bson.types.ObjectId;
import org.goblinframework.database.mongo.bson.deserializer.BsonObjectIdDeserializer;
import org.goblinframework.database.mongo.bson.introspect.BsonIntrospector;
import org.goblinframework.database.mongo.bson.serializer.BsonObjectIdSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

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
        serializers.addSerializer(ObjectId.class, new BsonObjectIdSerializer());
        context.addSerializers(serializers);

        BsonDeserializers deserializers = new BsonDeserializers();
        deserializers.addDeserializer(ObjectId.class, new BsonObjectIdDeserializer());
        context.addDeserializers(deserializers);
      }
    });
    return mapper;
  }

  public static ObjectMapper getDefaultObjectMapper() {
    return DEFAULT_OBJECT_MAPPER;
  }

  public static BsonDocument toDocument(@NotNull Map<String, Object> map) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DEFAULT_OBJECT_MAPPER.writeValue(bos, map);
    BsonDocument document = new BsonDocument();
    try (BsonBinaryReader reader = new BsonBinaryReader(ByteBuffer.wrap(bos.toByteArray()));
         BsonDocumentWriter writer = new BsonDocumentWriter(document)) {
      writer.pipe(reader);
      writer.flush();
    }
    return document;
  }

  public static <T> T fromBsonDocument(BsonDocument document, JavaType type) throws IOException {
    try (BsonDocumentReader reader = new BsonDocumentReader(document);
         BasicOutputBuffer output = new BasicOutputBuffer();
         BsonBinaryWriter writer = new BsonBinaryWriter(output)) {
      writer.pipe(reader);
      writer.flush();
      byte[] bytes = output.toByteArray();
      return DEFAULT_OBJECT_MAPPER.readValue(bytes, type);
    }
  }

  public static Map<String, ObjectId> fromBsonDocument(BsonDocument document) throws Exception {
    if (document == null) {
      return null;
    }
    JavaType theType = DEFAULT_OBJECT_MAPPER.getTypeFactory()
        .constructMapType(LinkedHashMap.class, String.class, ObjectId.class);
    return fromBsonDocument(document, theType);
  }
}
