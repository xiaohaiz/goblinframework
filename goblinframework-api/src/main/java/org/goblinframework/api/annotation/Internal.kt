package org.goblinframework.api.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Internal(
    val installRequired: Boolean = false,
    val uniqueInstance: Boolean = false
)
