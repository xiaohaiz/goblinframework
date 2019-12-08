package org.goblinframework.dao.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinCacheDimension {

  Dimension dimension();

  enum Dimension {

    NONE,                   // 没有缓存维度
    ID_FIELD,               // 仅在主键上有缓存维度
    OTHER_FIELDS,           // 在除主键外的其他字段上有缓存维度
    ID_AND_OTHER_FIELDS     // 在主键及其他字段上都有缓存维度

  }
}
