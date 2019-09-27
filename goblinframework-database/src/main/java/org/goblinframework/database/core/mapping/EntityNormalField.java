package org.goblinframework.database.core.mapping;

import org.goblinframework.core.util.GoblinField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

final public class EntityNormalField extends EntityField {

  public EntityNormalField(@NotNull EntityFieldNameResolver nameResolver, @NotNull GoblinField field) {
    super(nameResolver, field);
  }

  @Nullable
  @Override
  public Set<Class<?>> allowedFieldTypes() {
    return null;
  }
}
