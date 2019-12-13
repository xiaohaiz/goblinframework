package org.goblinframework.cache.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinCacheKeyPrefix(val prefix: String)
