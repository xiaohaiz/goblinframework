package org.goblinframework.embedded.core.provider;

import org.goblinframework.embedded.core.EmbeddedServer;
import org.goblinframework.embedded.core.EmbeddedServerFactory;
import org.goblinframework.embedded.core.EmbeddedServerMode;
import org.goblinframework.embedded.core.manager.EmbeddedServerSetting;
import org.jetbrains.annotations.NotNull;

public class JavaEmbeddedServerFactory implements EmbeddedServerFactory {

  @NotNull
  @Override
  public EmbeddedServerMode mode() {
    return EmbeddedServerMode.JAVA;
  }

  @NotNull
  @Override
  public EmbeddedServer createEmbeddedServer(@NotNull EmbeddedServerSetting setting) {
    return new JavaEmbeddedServer(setting);
  }
}
