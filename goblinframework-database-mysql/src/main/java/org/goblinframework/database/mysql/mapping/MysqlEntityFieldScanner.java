package org.goblinframework.database.mysql.mapping;

import org.goblinframework.api.annotation.Ignore;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.core.exception.GoblinMappingException;
import org.goblinframework.core.util.GoblinField;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.database.core.mapping.EntityFieldScanner;
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
  public List<GoblinField> scan(@NotNull Class<?> entityClass) {
    List<GoblinField> fields = ReflectionUtils.allFieldsIncludingAncestors(entityClass, false, false)
        .stream()
        .map(GoblinField::new)
        .filter(e -> !e.getField().isAnnotationPresent(Ignore.class))
        .collect(Collectors.toList());
    if (fields.stream().map(GoblinField::getFieldType).anyMatch(Class::isPrimitive)) {
      String errMsg = String.format("Primitive fields not allowed: %s", entityClass.getName());
      throw new GoblinMappingException(errMsg);
    }
    return fields;
  }
}
