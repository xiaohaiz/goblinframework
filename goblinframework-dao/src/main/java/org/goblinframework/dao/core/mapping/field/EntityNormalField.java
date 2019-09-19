package org.goblinframework.dao.core.mapping.field;

import org.goblinframework.core.exception.GoblinMappingException;
import org.goblinframework.core.reflection.Field;
import org.goblinframework.dao.core.mapping.EntityField;
import org.goblinframework.dao.core.mapping.EntityFieldNameResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

final public class EntityNormalField extends EntityField {

  public EntityNormalField(@NotNull EntityFieldNameResolver nameResolver, @NotNull Field field) {
    super(nameResolver, field);
    if (getAnnotation(org.goblinframework.api.annotation.Field.class) == null) {
      throw new GoblinMappingException("No @Field presented");
    }
  }

  @Nullable
  @Override
  public Set<Class<?>> allowedFieldTypes() {
    return null;
  }
}
