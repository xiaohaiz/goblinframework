package org.goblinframework.core.event

class EventBusConfig(val channel: String,
                     val ringBufferSize: Int,
                     val workHandlers: Int)