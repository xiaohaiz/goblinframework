package org.goblinframework.dao.mysql.mapping;

import org.goblinframework.api.annotation.Ignore;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.core.exception.GoblinMappingException;
import org.goblinframework.core.reflection.Field;
import org.goblinframework.core.reflection.ReflectionUtils;
import org.goblinframework.dao.core.mapping.EntityFieldScanner;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
final public class MysqlEntityFieldScanner implements EntityFieldScanner {

  public static final MysqlEntityFieldScanner INSTANCE = new MysqlEntityFieldScanner();

  private MysqlEntityFieldScanner() {
  }

  @NotNull
  @Override
  public List<Field> scan(@NotNull Class<?> entityClass) {
    List<Field> fields = ReflectionUtils.allFieldsIncludingAncestors(entityClass, false, false)
        .stream()
        .map(Field::new)
        .filter(e -> !e.getField().isAnnotationPresent(Ignore.class))
        .collect(Collectors.toList());
    if (fields.stream().map(Field::getFieldType).anyMatch(Class::isPrimitive)) {
      String errMsg = String.format("Primitive fields not allowed: %s", entityClass.getName());
      throw new GoblinMappingException(errMsg);
    }
    return fields;
  }
}
