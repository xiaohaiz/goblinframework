package org.goblinframework.database.core

/**
 * 标注在`persistence`和`dao`的实现类上，用于定义连接到哪个数据库的配置。
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinDatabaseConnection(val name: String)
