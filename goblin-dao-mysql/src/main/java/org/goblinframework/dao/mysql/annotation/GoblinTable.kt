package org.goblinframework.dao.mysql.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinTable(
    val table: String,
    val dynamic: Boolean = false
)
