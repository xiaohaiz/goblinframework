package org.goblinframework.monitor.message

import org.goblinframework.api.core.Singleton

@Singleton
class SnapshotMessageFactory private constructor()
  : TouchableMessageFactory<SnapshotMessage> {

  companion object {
    @JvmField val INSTANCE = SnapshotMessageFactory()
  }

  override fun newInstance(): SnapshotMessage {
    return SnapshotMessage()
  }
}