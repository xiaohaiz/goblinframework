package org.goblinframework.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.List;

final public class JsonUtils extends GoblinMappingSupport {

  private static final ObjectMapper DEFAULT_OBJECT_MAPPER;

  static {
    DEFAULT_OBJECT_MAPPER = createObjectMapper();
  }

  public static ObjectMapper getDefaultObjectMapper() {
    return DEFAULT_OBJECT_MAPPER;
  }

  public static ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    return mapper;
  }

  public static <E> List<E> asList(@NotNull InputStream inStream, @NotNull Class<E> elementType) {
    return asList(getDefaultObjectMapper(), inStream, elementType);
  }
}
