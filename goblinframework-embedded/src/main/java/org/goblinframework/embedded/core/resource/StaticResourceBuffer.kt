package org.goblinframework.embedded.core.resource

interface StaticResourceBuffer {

  fun loadFromBuffer(lookupPath: String): StaticResource?

  fun putIntoBuffer(lookupPath: String, staticResource: StaticResource)

}