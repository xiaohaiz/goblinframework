package org.goblinframework.core.event.timer

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.util.DateFormatUtils
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

@GoblinEventChannel("/goblin/timer")
class GoblinTimerEvent(val unit: TimeUnit, val sequence: Long) : GoblinEvent() {
  companion object {
    private const val serialVersionUID = 5355067363203991865L
  }

  override fun toString(): String {
    val createTime = DateFormatUtils.format(Date.from(source as Instant))
    return "GoblinTimerEvent[unit=$unit,sequence=$sequence,createTime=$createTime]"
  }
}
