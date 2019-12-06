package org.goblinframework.database.core.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Collection(
    val collection: String,
    val dynamic: Boolean = false
)
