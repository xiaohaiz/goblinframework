package org.goblinframework.embedded.core.provider;

import org.goblinframework.embedded.core.EmbeddedServer;
import org.goblinframework.embedded.core.manager.EmbeddedServerSetting;
import org.jetbrains.annotations.NotNull;

final public class JavaEmbeddedServer implements EmbeddedServer {

  private final EmbeddedServerSetting setting;

  JavaEmbeddedServer(@NotNull EmbeddedServerSetting setting) {
    this.setting = setting;
  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  @Override
  public boolean isRunning() {
    return false;
  }

  @Override
  public void close() {

  }
}
