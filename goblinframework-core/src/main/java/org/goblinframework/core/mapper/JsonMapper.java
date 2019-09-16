package org.goblinframework.core.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import org.bson.types.ObjectId;
import org.goblinframework.core.exception.GoblinMappingException;
import org.goblinframework.core.mapper.deserializer.ObjectIdDeserializer;
import org.goblinframework.core.mapper.serializer.ObjectIdSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

abstract public class JsonMapper {

  private static final ObjectMapper DEFAULT_OBJECT_MAPPER;

  static {
    DEFAULT_OBJECT_MAPPER = createObjectMapper();
  }

  private static ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.registerModule(new SimpleModule() {
      @Override
      public void setupModule(SetupContext context) {
        super.setupModule(context);

        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(ObjectId.class, new ObjectIdSerializer());
        context.addSerializers(serializers);

        SimpleDeserializers deserializers = new SimpleDeserializers();
        deserializers.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
        context.addDeserializers(deserializers);
      }
    });
    return mapper;
  }

  public static ObjectMapper getDefaultObjectMapper() {
    return DEFAULT_OBJECT_MAPPER;
  }

  public static String toJson(@Nullable Object value) {
    try {
      return DEFAULT_OBJECT_MAPPER.writeValueAsString(value);
    } catch (JsonProcessingException ex) {
      throw new GoblinMappingException(ex);
    }
  }

  public static <E> E asObject(@NotNull InputStream inStream,
                               @NotNull Class<E> valueType) {
    try {
      return getDefaultObjectMapper().readValue(inStream, valueType);
    } catch (IOException ex) {
      throw new GoblinMappingException(ex);
    }
  }

  public static <E> E asObject(@NotNull String s,
                               @NotNull Class<E> valueType) {
    try {
      return getDefaultObjectMapper().readValue(s, valueType);
    } catch (IOException ex) {
      throw new GoblinMappingException(ex);
    }
  }

  public static <E> List<E> asList(@NotNull InputStream inStream,
                                   @NotNull Class<E> elementType) {
    JavaType jt = getDefaultObjectMapper().getTypeFactory().constructCollectionLikeType(LinkedList.class, elementType);
    try {
      return getDefaultObjectMapper().readValue(inStream, jt);
    } catch (IOException ex) {
      throw new GoblinMappingException(ex);
    }
  }
}
