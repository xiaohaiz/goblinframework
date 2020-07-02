package org.goblinframework.embedded.resource

interface StaticResourceManager {

  fun getLookupPathPrefix(): String

  fun lookup(lookupPath: String): StaticResource?
}
