package org.goblinframework.core.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import java.io.InputStream
import java.util.*

object YamlUtils {

  val defaultObjectMapper: YAMLMapper

  init {
    defaultObjectMapper = createObjectMapper()
  }

  fun createObjectMapper(): YAMLMapper {
    val mapper = YAMLMapper()
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    mapper.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
    return mapper
  }

  fun <E> asList(inStream: InputStream, elementType: Class<E>): List<E> {
    val jt = defaultObjectMapper.typeFactory.constructCollectionLikeType(LinkedList::class.java, elementType)
    return defaultObjectMapper.readValue(inStream, jt)
  }
}
