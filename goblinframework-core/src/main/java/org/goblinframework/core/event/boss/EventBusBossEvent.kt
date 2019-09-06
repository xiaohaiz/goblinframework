package org.goblinframework.core.event.boss

import org.goblinframework.core.event.context.GoblinEventContextImpl

class EventBusBossEvent {

  var ctx: GoblinEventContextImpl? = null

  internal fun clear() {
    ctx = null
  }

}