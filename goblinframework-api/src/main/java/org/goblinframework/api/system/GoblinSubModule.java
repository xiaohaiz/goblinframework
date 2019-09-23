package org.goblinframework.api.system;

import org.jetbrains.annotations.NotNull;

public enum GoblinSubModule {

  EMBEDDED_JETTY(GoblinModule.EMBEDDED),
  EMBEDDED_NETTY(GoblinModule.EMBEDDED),
  CACHE_COUCHBASE(GoblinModule.CACHE),
  CACHE_REDIS(GoblinModule.CACHE),
  DAO_MONGO(GoblinModule.DAO),
  DAO_MYSQL(GoblinModule.DAO),
  TRANSPORT_CLIENT(GoblinModule.TRANSPORT),
  TRANSPORT_SERVER(GoblinModule.TRANSPORT),
  REGISTRY_ZOOKEEPER(GoblinModule.REGISTRY),
  REMOTE_CLIENT(GoblinModule.REMOTE),
  REMOTE_SERVER(GoblinModule.REMOTE);

  private final GoblinModule parent;

  GoblinSubModule(@NotNull GoblinModule parent) {
    this.parent = parent;
  }

  @NotNull
  public GoblinModule parent() {
    return parent;
  }

  @NotNull
  public String fullName() {
    int idx = name().indexOf("_");
    String name;
    if (idx == -1) {
      name = name();
    } else {
      name = name().substring(idx + 1);
    }
    return parent.name() + ":" + name;
  }
}
