package org.goblinframework.dao.mapping;

import org.goblinframework.core.util.GoblinField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;

final public class EntityUpdateTimeField extends EntityField {

  private static final Set<Class<?>> ALLOWED;

  static {
    Set<Class<?>> allowed = new HashSet<>();
    allowed.add(Long.class);
    allowed.add(Date.class);
    allowed.add(Calendar.class);
    allowed.add(Instant.class);
    allowed.add(String.class);
    ALLOWED = Collections.unmodifiableSet(allowed);
  }

  public EntityUpdateTimeField(@NotNull EntityFieldNameResolver nameResolver, @NotNull GoblinField field) {
    super(nameResolver, field);
  }

  @Nullable
  @Override
  public Set<Class<?>> allowedFieldTypes() {
    return ALLOWED;
  }
}
