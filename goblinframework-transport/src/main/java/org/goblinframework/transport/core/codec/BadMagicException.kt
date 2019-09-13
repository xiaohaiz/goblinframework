package org.goblinframework.transport.core.codec

import org.goblinframework.api.common.GoblinException

class BadMagicException : GoblinException() {
  companion object {
    private const val serialVersionUID = -7638210891605367045L
  }
}
