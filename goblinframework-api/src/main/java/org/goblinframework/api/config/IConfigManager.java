package org.goblinframework.api.config;

import org.goblinframework.api.common.Internal;
import org.jetbrains.annotations.NotNull;

@Internal(installRequired = true, uniqueInstance = true)
public interface IConfigManager {

  void registerConfigParser(@NotNull ConfigParser parser);

  void registerConfigListener(@NotNull ConfigListener listener);

  @NotNull
  static IConfigManager instance() {
    IConfigManager installed = ConfigManagerInstaller.INSTALLED;
    assert installed != null;
    return installed;
  }
}
