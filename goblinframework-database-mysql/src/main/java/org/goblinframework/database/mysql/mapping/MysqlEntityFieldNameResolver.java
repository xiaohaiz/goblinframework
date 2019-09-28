package org.goblinframework.database.mysql.mapping;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.database.Field;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.database.core.mapping.EntityField;
import org.goblinframework.database.core.mapping.EntityFieldNameResolver;
import org.jetbrains.annotations.NotNull;

@Singleton
final public class MysqlEntityFieldNameResolver implements EntityFieldNameResolver {

  public static final MysqlEntityFieldNameResolver INSTANCE = new MysqlEntityFieldNameResolver();

  private MysqlEntityFieldNameResolver() {
  }

  @NotNull
  @Override
  public String resolve(@NotNull EntityField field) {
    Field annotation = field.getAnnotation(Field.class);
    if (annotation != null && StringUtils.isNotBlank(annotation.value())) {
      return annotation.value().trim();
    }
    StringBuilder sbuf = new StringBuilder();
    char[] chars = field.getField().getFieldName().toCharArray();
    for (char c : chars) {
      if (Character.isUpperCase(c)) {
        sbuf.append("_").append(c);
      } else {
        sbuf.append(Character.toUpperCase(c));
      }
    }
    return sbuf.toString();
  }
}
