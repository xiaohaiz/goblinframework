package org.goblinframework.core.service

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinManagedBean(
    val type: String = "",
    val name: String = "",
    val register: Boolean = true
)
