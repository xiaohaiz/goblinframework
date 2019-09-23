package org.goblinframework.api.system;

import org.jetbrains.annotations.NotNull;

public enum GoblinSubModule {

  EMBEDDED_JETTY(GoblinModule.EMBEDDED),
  CACHE_REDIS(GoblinModule.CACHE);

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
