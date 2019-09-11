package org.goblinframework.core.module.spi

import org.goblinframework.core.event.GoblinEventListener

interface SubscribeEventBusChannel {

  fun subscribed(channel: String, listener: GoblinEventListener)

}