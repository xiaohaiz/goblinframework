package org.goblinframework.registry.zookeeper;

import org.jetbrains.annotations.NotNull;

final public class ZookeeperClientFactory {

  @NotNull
  public static ZookeeperClient createZookeeperClient(@NotNull ZkClientSetting setting) {
    return new ZookeeperClient(setting);
  }
}
