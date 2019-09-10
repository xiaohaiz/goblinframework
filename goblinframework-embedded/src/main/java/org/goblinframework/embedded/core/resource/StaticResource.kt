package org.goblinframework.embedded.core.resource

import java.io.Serializable

class StaticResource(val content: ByteArray?,
                     val contentType: String?,
                     val lastModified: Long) : Serializable {
  companion object {
    private const val serialVersionUID = 4561083774846127507L
  }
}
