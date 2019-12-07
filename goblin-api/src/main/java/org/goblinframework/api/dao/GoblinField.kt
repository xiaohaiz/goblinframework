package org.goblinframework.api.dao

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinField(val value: String = "")
