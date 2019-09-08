package org.goblinframework.embedded.core.provider;

import org.goblinframework.embedded.core.EmbeddedServer;
import org.goblinframework.embedded.core.setting.EmbeddedServerSetting;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

final public class JavaEmbeddedServer implements EmbeddedServer {

  private final EmbeddedServerSetting setting;
  private final AtomicReference<JavaEmbeddedServerImpl> server = new AtomicReference<>();

  JavaEmbeddedServer(@NotNull EmbeddedServerSetting setting) {
    this.setting = setting;
  }

  @Override
  public synchronized void start() {
    if (server.get() != null) {
      return;
    }
    server.set(new JavaEmbeddedServerImpl(setting));
  }

  @Override
  public synchronized void stop() {
    JavaEmbeddedServerImpl s = server.getAndSet(null);
    if (s != null) {
      s.stop();
    }
  }

  @Override
  public synchronized boolean isRunning() {
    return server.get() != null;
  }

}
