package org.goblinframework.core.event

class EventBusBossEvent {

  var ctx: GoblinEventContextImpl? = null

  internal fun clear() {
    ctx = null
  }

}