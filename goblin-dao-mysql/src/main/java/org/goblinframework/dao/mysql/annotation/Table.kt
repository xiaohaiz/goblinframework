package org.goblinframework.dao.mysql.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Table(
    val table: String,
    val dynamic: Boolean = false
)
