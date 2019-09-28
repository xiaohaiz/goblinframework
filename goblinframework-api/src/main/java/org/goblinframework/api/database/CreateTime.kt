package org.goblinframework.api.database

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CreateTime(val pattern: String = "yyyy-MM-dd HH:mm:ss.SSS")
