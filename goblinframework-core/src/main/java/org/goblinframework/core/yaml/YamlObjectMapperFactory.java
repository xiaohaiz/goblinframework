package org.goblinframework.core.yaml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

final public class YamlObjectMapperFactory {

  public static final YAMLMapper DEFAULT_OBJECT_MAPPER;

  static {
    DEFAULT_OBJECT_MAPPER = createObjectMapper();
  }

  public static YAMLMapper createObjectMapper() {
    YAMLMapper mapper = new YAMLMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
    return mapper;
  }
}
