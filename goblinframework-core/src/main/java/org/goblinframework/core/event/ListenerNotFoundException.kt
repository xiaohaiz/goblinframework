package org.goblinframework.core.event

class ListenerNotFoundException(channel: String) : RuntimeException("No listener found on channel [$channel]") {
  companion object {
    private const val serialVersionUID = 3538007180442977756L
  }
}
