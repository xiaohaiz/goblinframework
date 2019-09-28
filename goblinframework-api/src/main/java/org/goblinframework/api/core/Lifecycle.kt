package org.goblinframework.api.core

interface Lifecycle {

  fun isRunning(): Boolean

  fun start()

  fun stop()

}
