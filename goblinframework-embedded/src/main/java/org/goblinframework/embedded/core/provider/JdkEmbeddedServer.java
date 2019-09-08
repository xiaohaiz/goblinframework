package org.goblinframework.embedded.core.provider;

import org.goblinframework.embedded.core.EmbeddedServer;
import org.goblinframework.embedded.core.setting.EmbeddedServerSetting;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

final public class JdkEmbeddedServer implements EmbeddedServer {

  private final EmbeddedServerSetting setting;
  private final AtomicReference<JdkEmbeddedServerImpl> server = new AtomicReference<>();

  JdkEmbeddedServer(@NotNull EmbeddedServerSetting setting) {
    this.setting = setting;
  }

  @Override
  public synchronized void start() {
    if (server.get() != null) {
      return;
    }
    server.set(new JdkEmbeddedServerImpl(setting));
  }

  @Override
  public synchronized void stop() {
    JdkEmbeddedServerImpl s = server.getAndSet(null);
    if (s != null) {
      s.stop();
    }
  }

  @Override
  public synchronized boolean isRunning() {
    return server.get() != null;
  }

}
