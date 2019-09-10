package org.goblinframework.core.container

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class SpringContainer(vararg val value: String)
