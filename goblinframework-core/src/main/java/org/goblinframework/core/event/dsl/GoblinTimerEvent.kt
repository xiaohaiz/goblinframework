package org.goblinframework.core.event.dsl

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventChannel
import java.util.concurrent.TimeUnit

@GoblinEventChannel("/goblin/timer")
class GoblinTimerEvent(val unit: TimeUnit, val sequence: Long) : GoblinEvent() {
  companion object {
    private const val serialVersionUID = 5355067363203991865L
  }

  override fun toString(): String {
    return "GoblinTimerEvent[unit=$unit,sequence=$sequence]"
  }
}
