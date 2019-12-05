package org.goblinframework.api.dao

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Collection(
    val collection: String,
    val dynamic: Boolean = false
)
