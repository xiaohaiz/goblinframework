package org.goblinframework.registry.listener;

import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.registry.core.RegistryState;
import org.goblinframework.registry.core.RegistryStateListener;
import org.jetbrains.annotations.NotNull;

final public class ZookeeperStateListener implements IZkStateListener {

  @NotNull private final RegistryStateListener listener;

  ZookeeperStateListener(@NotNull RegistryStateListener listener) {
    this.listener = listener;
  }

  @Override
  public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
    if (state == null) {
      return;
    }
    RegistryState rs = ConversionService.INSTANCE.convert(state, RegistryState.class);
    if (rs == null) {
      return;
    }
    listener.onStateChanged(rs);
  }

  @Override
  public void handleNewSession() {
  }

  @Override
  public void handleSessionEstablishmentError(Throwable error) {
  }
}
