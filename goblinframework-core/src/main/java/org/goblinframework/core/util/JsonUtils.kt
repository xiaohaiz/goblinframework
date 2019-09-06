package org.goblinframework.core.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import java.io.InputStream
import java.util.*

object JsonUtils {

  val defaultObjectMapper: ObjectMapper

  init {
    defaultObjectMapper = createObjectMapper()
  }

  fun createObjectMapper(): ObjectMapper {
    val mapper = ObjectMapper()
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    return mapper
  }

  fun <E> asList(inStream: InputStream, elementType: Class<E>): List<E> {
    val jt = YamlUtils.defaultObjectMapper.typeFactory.constructCollectionLikeType(LinkedList::class.java, elementType)
    return YamlUtils.defaultObjectMapper.readValue(inStream, jt)
  }
}
