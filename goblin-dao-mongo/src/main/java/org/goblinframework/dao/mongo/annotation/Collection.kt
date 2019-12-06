package org.goblinframework.dao.mongo.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Collection(
    val collection: String,
    val dynamic: Boolean = false
)
