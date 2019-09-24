package org.goblinframework.registry.zookeeper.provider;

import org.I0Itec.zkclient.IZkChildListener;
import org.goblinframework.api.registry.RegistryChildListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final public class ZookeeperChildListener implements IZkChildListener {

  private final RegistryChildListener listener;

  ZookeeperChildListener(@NotNull RegistryChildListener listener) {
    this.listener = listener;
  }

  @Override
  public void handleChildChange(String parentPath, List<String> currentChildren) {
    if (currentChildren == null) {
      listener.onParentPathDeleted(parentPath);
    } else {
      listener.onChildChanged(parentPath, currentChildren);
    }
  }
}
