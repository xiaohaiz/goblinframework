package org.goblinframework.dao.core.mapping;

import org.apache.commons.lang3.mutable.MutableObject;
import org.goblinframework.core.exception.GoblinMappingException;
import org.goblinframework.core.util.GoblinField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Set;

abstract public class EntityField {

  private final EntityFieldNameResolver nameResolver;
  private final GoblinField field;
  private final MutableObject<String> resolvedName = new MutableObject<>(null);

  public EntityField(@NotNull EntityFieldNameResolver nameResolver,
                     @NotNull GoblinField field) {
    this.nameResolver = nameResolver;
    this.field = field;

    Set<Class<?>> allowed = allowedFieldTypes();
    if (allowed != null) {
      Class<?> fieldType = field.getFieldType();
      if (!allowed.contains(fieldType)) {
        throw new GoblinMappingException("Field type [" + fieldType.getName()
            + "] not allowed when creating [" + getClass().getName() + "]");
      }
    }
  }

  @Nullable
  public <T extends Annotation> T getAnnotation(@NotNull Class<T> annotationClass) {
    return field.getField().getAnnotation(annotationClass);
  }

  @NotNull
  public synchronized String getName() {
    String resolved = resolvedName.getValue();
    if (resolved != null) return resolved;
    resolved = nameResolver.resolve(this);
    resolvedName.setValue(resolved);
    return resolved;
  }

  @NotNull
  public GoblinField getField() {
    return field;
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
