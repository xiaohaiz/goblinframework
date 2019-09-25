package org.goblinframework.api.dao;

import java.lang.annotation.*;

/**
 * 标注在{@code persistence}和{@code dao}的实现类上，用于定义连接到哪个数据库的配置。
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoblinDatabaseConnection {

  String name();

}
