package org.goblinframework.core.service

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinManagedStopWatch(val autoStart: Boolean = true)
