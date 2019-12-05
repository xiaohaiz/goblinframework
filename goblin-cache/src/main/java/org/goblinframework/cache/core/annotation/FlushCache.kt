package org.goblinframework.cache.core.annotation

import org.goblinframework.cache.core.cache.CacheSystem

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class FlushCache(val system: CacheSystem, val name: String)
