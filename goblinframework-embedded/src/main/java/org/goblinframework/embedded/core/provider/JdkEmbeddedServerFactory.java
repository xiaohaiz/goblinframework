package org.goblinframework.embedded.core.provider;

import org.goblinframework.embedded.core.EmbeddedServer;
import org.goblinframework.embedded.core.EmbeddedServerMode;
import org.goblinframework.embedded.core.module.spi.EmbeddedServerFactory;
import org.goblinframework.embedded.core.setting.ServerSetting;
import org.jetbrains.annotations.NotNull;

public class JdkEmbeddedServerFactory implements EmbeddedServerFactory {

  @NotNull
  @Override
  public EmbeddedServerMode mode() {
    return EmbeddedServerMode.JDK;
  }

  @NotNull
  @Override
  public EmbeddedServer createEmbeddedServer(@NotNull ServerSetting setting) {
    return new JdkEmbeddedServer(setting);
  }
}
