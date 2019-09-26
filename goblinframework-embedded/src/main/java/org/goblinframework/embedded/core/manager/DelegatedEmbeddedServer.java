package org.goblinframework.embedded.core.manager;

import org.goblinframework.api.core.GoblinManagedBean;
import org.goblinframework.api.core.GoblinManagedObject;
import org.goblinframework.embedded.core.EmbeddedServer;
import org.jetbrains.annotations.NotNull;

@GoblinManagedBean(type = "EMBEDDED", name = "EmbeddedServer")
final class DelegatedEmbeddedServer extends GoblinManagedObject
    implements EmbeddedServer, EmbeddedServerMXBean {

  private final EmbeddedServer server;

  DelegatedEmbeddedServer(@NotNull EmbeddedServer server) {
    this.server = server;
  }

  @Override
  public void start() {
    server.start();
  }

  @Override
  public void stop() {
    server.stop();
  }

  @Override
  public boolean isRunning() {
    return server.isRunning();
  }

}
