package org.goblinframework.api.container;

import org.goblinframework.api.common.Internal;
import org.jetbrains.annotations.NotNull;

@Internal(installRequired = true, uniqueInstance = true)
public interface ISpringContainerManager {

  @NotNull
  Object createStandaloneContainer(@NotNull String... configLocations);

  @NotNull
  static ISpringContainerManager instance() {
    ISpringContainerManager installed = SpringContainerManagerInstaller.INSTALLED;
    assert installed != null;
    return installed;
  }
}
