package org.goblinframework.dao.core.mapping.field;

import org.bson.types.ObjectId;
import org.goblinframework.core.util.GoblinField;
import org.goblinframework.dao.core.mapping.EntityField;
import org.goblinframework.dao.core.mapping.EntityFieldNameResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final public class EntityIdField extends EntityField {

  private static final Set<Class<?>> ALLOWED;

  static {
    Set<Class<?>> allowed = new HashSet<>();
    allowed.add(Long.class);
    allowed.add(Integer.class);
    allowed.add(String.class);
    allowed.add(ObjectId.class);
    ALLOWED = Collections.unmodifiableSet(allowed);
  }

  public EntityIdField(@NotNull EntityFieldNameResolver nameResolver, @NotNull GoblinField field) {
    super(nameResolver, field);
  }

  @Nullable
  @Override
  public Set<Class<?>> allowedFieldTypes() {
    return ALLOWED;
  }
}
