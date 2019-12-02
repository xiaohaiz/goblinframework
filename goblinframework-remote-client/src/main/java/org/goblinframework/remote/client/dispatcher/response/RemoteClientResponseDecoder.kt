package org.goblinframework.remote.client.dispatcher.response

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("RemoteClient")
class RemoteClientResponseDecoder internal constructor()
  : GoblinManagedObject(), RemoteClientResponseDecoderMXBean {
}