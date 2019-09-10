package org.goblinframework.embedded.core.resource

interface StaticResourceManager {

  fun getLookupPathPrefix(): String

  fun lookup(lookupPath: String): StaticResource?
}
