package org.goblinframework.database.core.mapping;

import org.jetbrains.annotations.NotNull;

public interface EntityFieldNameResolver {

  @NotNull
  String resolve(@NotNull EntityField field);

}
