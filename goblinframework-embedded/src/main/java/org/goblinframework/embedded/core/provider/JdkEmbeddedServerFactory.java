package org.goblinframework.embedded.core.provider;

import org.goblinframework.embedded.core.EmbeddedServer;
import org.goblinframework.embedded.core.EmbeddedServerFactory;
import org.goblinframework.embedded.core.EmbeddedServerMode;
import org.goblinframework.embedded.core.setting.EmbeddedServerSetting;
import org.jetbrains.annotations.NotNull;

public class JdkEmbeddedServerFactory implements EmbeddedServerFactory {

  @NotNull
  @Override
  public EmbeddedServerMode mode() {
    return EmbeddedServerMode.JDK;
  }

  @NotNull
  @Override
  public EmbeddedServer createEmbeddedServer(@NotNull EmbeddedServerSetting setting) {
    return new JdkEmbeddedServer(setting);
  }
}
