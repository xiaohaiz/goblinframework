package org.goblinframework.embedded.core.provider;

import org.goblinframework.embedded.core.setting.EmbeddedServerSetting;
import org.jetbrains.annotations.NotNull;

final class JavaEmbeddedServerImpl {

  private final EmbeddedServerSetting setting;

  JavaEmbeddedServerImpl(@NotNull EmbeddedServerSetting setting) {
    this.setting = setting;
  }

  void stop() {
  }
}
