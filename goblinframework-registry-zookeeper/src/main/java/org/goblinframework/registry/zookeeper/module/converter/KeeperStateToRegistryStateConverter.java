package org.goblinframework.registry.zookeeper.module.converter;

import org.apache.zookeeper.Watcher;
import org.goblinframework.api.common.Install;
import org.goblinframework.api.registry.RegistryState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;

@Install
final public class KeeperStateToRegistryStateConverter implements Converter<Watcher.Event.KeeperState, RegistryState> {

  @Nullable
  @Override
  public RegistryState convert(@NotNull Watcher.Event.KeeperState source) {
    switch (source) {
      case SyncConnected:
        return RegistryState.CONNECTED;
      case Disconnected:
        return RegistryState.DISCONNECTED;
      case Closed:
        return RegistryState.CLOSED;
      default:
        return null;
    }
  }
}
