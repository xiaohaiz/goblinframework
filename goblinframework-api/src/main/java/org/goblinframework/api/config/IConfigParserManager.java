package org.goblinframework.api.config;

import org.goblinframework.api.annotation.Internal;
import org.jetbrains.annotations.NotNull;

@Internal(installRequired = true, uniqueInstance = true)
public interface IConfigParserManager {

  void register(@NotNull ConfigParser parser);

  @NotNull
  static IConfigParserManager instance() {
    IConfigParserManager installed = ConfigParserManagerInstaller.INSTALLED;
    assert installed != null;
    return installed;
  }
}
