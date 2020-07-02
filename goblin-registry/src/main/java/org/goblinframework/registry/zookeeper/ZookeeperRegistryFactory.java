package org.goblinframework.registry.zookeeper;

import org.jetbrains.annotations.NotNull;

final public class ZookeeperRegistryFactory {

  @NotNull
  public static ZookeeperRegistry createZookeeperRegistry(@NotNull ZookeeperClient client) {
    return new ZookeeperRegistry(client);
  }
}
