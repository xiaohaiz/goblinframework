package org.goblinframework.dao.core.mapping;

import org.goblinframework.core.reflection.Field;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EntityFieldScanner {

  @NotNull
  List<Field> scan(@NotNull Class<?> entityClass);

}
