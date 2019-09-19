package org.goblinframework.dao.core.mapping.field;

import org.goblinframework.api.annotation.UpdateTime;
import org.goblinframework.core.exception.GoblinMappingException;
import org.goblinframework.core.reflection.Field;
import org.goblinframework.dao.core.mapping.EntityField;
import org.goblinframework.dao.core.mapping.EntityFieldNameResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;

public class EntityUpdateTimeField extends EntityField {

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

  public EntityUpdateTimeField(@NotNull EntityFieldNameResolver nameResolver, @NotNull Field field) {
    super(nameResolver, field);
    if (getAnnotation(UpdateTime.class) == null) {
      throw new GoblinMappingException("No @UpdateTime presented");
    }
  }

  @Nullable
  @Override
  public Set<Class<?>> allowedFieldTypes() {
    return ALLOWED;
  }
}
