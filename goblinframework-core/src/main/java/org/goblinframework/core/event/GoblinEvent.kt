package org.goblinframework.core.event

import java.time.Instant
import java.util.*

abstract class GoblinEvent(val fair: Boolean = false) : EventObject(Instant.now()) {
  companion object {
    private const val serialVersionUID = 1272680749868964006L
  }
}
