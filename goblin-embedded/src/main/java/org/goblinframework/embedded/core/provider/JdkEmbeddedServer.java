package org.goblinframework.embedded.core.provider;

import org.goblinframework.embedded.core.setting.ServerSetting;
import org.goblinframework.embedded.server.EmbeddedServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

final public class JdkEmbeddedServer implements EmbeddedServer {
  private static final Logger logger = LoggerFactory.getLogger(JdkEmbeddedServer.class);

  private final ServerSetting setting;
  private final AtomicReference<JdkEmbeddedServerImpl> server = new AtomicReference<>();

  JdkEmbeddedServer(@NotNull ServerSetting setting) {
    this.setting = setting;
  }

  @Override
  public synchronized void start() {
    if (server.get() != null) {
      return;
    }
    server.set(new JdkEmbeddedServerImpl(setting));
    if (logger.isDebugEnabled()) {
      logger.debug("JDK embedded server [{}] started at [{}:{}]",
          setting.name(), server.get().getHost(), server.get().getPort());
    }
  }

  @Override
  public synchronized void stop() {
    JdkEmbeddedServerImpl s = server.getAndSet(null);
    if (s != null) {
      s.stop();
      if (logger.isDebugEnabled()) {
        logger.debug("JDK embedded server [{}] stopped", setting.name());
      }
    }
  }

  @Override
  public synchronized boolean isRunning() {
    return server.get() != null;
  }

}
