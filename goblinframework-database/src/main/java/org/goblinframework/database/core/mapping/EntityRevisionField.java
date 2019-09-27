package org.goblinframework.database.core.mapping;

import org.goblinframework.core.util.GoblinField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final public class EntityRevisionField extends EntityField {

  private static final Set<Class<?>> ALLOWED;

  static {
    Set<Class<?>> allowed = new HashSet<>();
    allowed.add(Long.class);
    allowed.add(Integer.class);
    ALLOWED = Collections.unmodifiableSet(allowed);
  }

  public EntityRevisionField(@NotNull EntityFieldNameResolver nameResolver, @NotNull GoblinField field) {
    super(nameResolver, field);
  }

  @Nullable
  @Override
  public Set<Class<?>> allowedFieldTypes() {
    return ALLOWED;
  }
}
