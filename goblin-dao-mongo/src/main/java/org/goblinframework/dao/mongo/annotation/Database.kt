package org.goblinframework.dao.mongo.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Database(
    val database: String,
    val dynamic: Boolean = false
)
