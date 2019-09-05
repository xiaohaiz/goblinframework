package org.goblinframework.core.event

enum class GoblinEventState {

  SUCCESS,
  FAILURE,
  EVENT_BUS_RING_BUFFER_FULL,
  CHANNEL_NOT_REGISTERED,
  LISTENER_NOT_SUBSCRIBED

}