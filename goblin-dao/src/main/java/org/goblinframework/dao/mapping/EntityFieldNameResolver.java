package org.goblinframework.dao.mapping;

import org.jetbrains.annotations.NotNull;

public interface EntityFieldNameResolver {

  @NotNull
  String resolve(@NotNull EntityField field);

}
