package org.goblinframework.database.core.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Table(
    val table: String,
    val dynamic: Boolean = false
)
