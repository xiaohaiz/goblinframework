package org.goblinframework.core.event

class WorkerRingBufferFullException(channel: String) : RuntimeException("Ring buffer of channel [$channel] full") {
  companion object {
    private const val serialVersionUID = 520286830054642967L
  }
}
