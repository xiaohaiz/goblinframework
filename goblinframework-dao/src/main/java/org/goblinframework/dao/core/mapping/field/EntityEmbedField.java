package org.goblinframework.dao.core.mapping.field;

import org.goblinframework.core.util.GoblinField;
import org.goblinframework.core.util.GoblinReflectionException;
import org.goblinframework.dao.core.mapping.EntityField;
import org.goblinframework.dao.core.mapping.EntityFieldNameResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

final public class EntityEmbedField extends EntityField {

  private final GoblinField parent;

  public EntityEmbedField(@NotNull EntityFieldNameResolver nameResolver,
                          @NotNull GoblinField child,
                          @NotNull GoblinField parent) {
    super(nameResolver, child);
    this.parent = parent;
  }

  public GoblinField getParent() {
    return parent;
  }

  public GoblinField getChild() {
    return getField();
  }

  @Override
  public Object getValue(@NotNull Object obj) {
    Object embed = instantiateEmbedFieldIfNecessary(obj);
    return getChild().get(embed);
  }

  @Override
  public void setValue(@NotNull Object obj, Object value) {
    Object embed = instantiateEmbedFieldIfNecessary(obj);
    getChild().set(embed, value);
  }

  @Nullable
  @Override
  public Set<Class<?>> allowedFieldTypes() {
    return null;
  }

  private Object instantiateEmbedFieldIfNecessary(Object obj) {
    Object embed = parent.get(obj);
    if (embed == null) {
      Class<?> embedDocumentClass = parent.getFieldType();
      try {
        embed = embedDocumentClass.newInstance();
      } catch (Exception ex) {
        throw new GoblinReflectionException(ex);
      }
      parent.set(obj, embed);
    }
    return embed;
  }
}
