package org.goblinframework.transport.core.codec

import org.goblinframework.core.exception.GoblinException

@Deprecated("TBR")
class SerializerNotFoundException(serializer: Byte)
  : GoblinException("Serializer [$serializer] not found") {

  companion object {
    private const val serialVersionUID = -49980278235204717L
  }

}
