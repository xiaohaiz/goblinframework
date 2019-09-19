package org.goblinframework.dao.core.mapping;

import org.goblinframework.core.reflection.Field;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

abstract public class EntityField {

  protected final EntityFieldNameResolver nameResolver;
  protected final Field field;

  public EntityField(@NotNull EntityFieldNameResolver nameResolver,
                     @NotNull Field field) {
    this.nameResolver = nameResolver;
    this.field = field;
  }

  @NotNull
  public String getName() {
    return nameResolver.resolve(this);
  }

  public Object getValue(@NotNull Object obj) {
    return field.get(obj);
  }

  public void setValue(@NotNull Object obj, Object value) {
    field.set(obj, value);
  }

  @Nullable
  abstract public Set<Class<?>> allowedFieldTypes();
}
