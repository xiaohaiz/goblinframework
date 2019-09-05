package org.goblinframework.core.event.config

class EventBusConfig(val channel: String,
                     val ringBufferSize: Int,
                     val workHandlers: Int)