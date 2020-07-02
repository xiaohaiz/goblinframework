package org.goblinframework.dao.mapping;

import org.goblinframework.core.util.GoblinField;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EntityFieldScanner {

  @NotNull
  List<GoblinField> scan(@NotNull Class<?> entityClass);

}
