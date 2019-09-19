package org.goblinframework.dao.core.mapping.field;

import org.goblinframework.core.reflection.Field;
import org.goblinframework.dao.core.mapping.EntityField;
import org.goblinframework.dao.core.mapping.EntityFieldNameResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

final public class EntityNormalField extends EntityField {

  public EntityNormalField(@NotNull EntityFieldNameResolver nameResolver, @NotNull Field field) {
    super(nameResolver, field);
  }

  @Nullable
  @Override
  public Set<Class<?>> allowedFieldTypes() {
    return null;
  }
}
