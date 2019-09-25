package org.goblinframework.core.event.worker

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class EventBusConfig
@JsonCreator constructor(@JsonProperty("channel") val channel: String,
                         @JsonProperty("ringBufferSize") val ringBufferSize: Int,
                         @JsonProperty("workHandlers") val workHandlers: Int)