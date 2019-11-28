package org.goblinframework.registry.listener;

import org.I0Itec.zkclient.IZkChildListener;
import org.goblinframework.registry.core.RegistryChildListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final public class ZookeeperChildListener implements IZkChildListener {

  @NotNull private final RegistryChildListener listener;

  ZookeeperChildListener(@NotNull RegistryChildListener listener) {
    this.listener = listener;
  }

  @Override
  public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
    if (currentChildren == null) {
      listener.onParentPathDeleted(parentPath);
    } else {
      listener.onChildChanged(parentPath, currentChildren);
    }
  }
}
