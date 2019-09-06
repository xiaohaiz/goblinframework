package org.goblinframework.core.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

abstract class GoblinMappingSupport {

  static <E> List<E> asList(@NotNull ObjectMapper mapper,
                            @NotNull InputStream inStream,
                            @NotNull Class<E> elementType) {
    JavaType jt = mapper.getTypeFactory().constructCollectionLikeType(LinkedList.class, elementType);
    try {
      return mapper.readValue(inStream, jt);
    } catch (IOException ex) {
      throw new GoblinMappingException(ex);
    }
  }

}