package org.goblinframework.embedded.resource

import org.apache.commons.collections4.map.LRUMap

class MapStaticResourceBuffer(bufferSize: Int) : StaticResourceBuffer {

  private val buffer = LRUMap<String, StaticResource>(bufferSize)

  override fun loadFromBuffer(lookupPath: String): StaticResource? {
    return buffer[lookupPath]
  }

  override fun putIntoBuffer(lookupPath: String, staticResource: StaticResource) {
    buffer[lookupPath] = staticResource
  }
}