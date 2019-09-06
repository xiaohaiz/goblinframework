package org.goblinframework.core.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

public interface GoblinMapper {

  @NotNull
  ObjectMapper getNativeMapper();

}
