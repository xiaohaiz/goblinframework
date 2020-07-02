package org.goblinframework.embedded.resource

class NopStaticResourceBuffer private constructor() : StaticResourceBuffer {

  companion object {
    @JvmField val INSTANCE = NopStaticResourceBuffer()
  }

  override fun loadFromBuffer(lookupPath: String): StaticResource? {
    return null
  }

  override fun putIntoBuffer(lookupPath: String, staticResource: StaticResource) {
  }
}