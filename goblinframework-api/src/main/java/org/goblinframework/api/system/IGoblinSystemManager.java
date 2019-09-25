package org.goblinframework.api.system;

import org.goblinframework.api.common.Internal;
import org.goblinframework.api.common.Lifecycle;
import org.jetbrains.annotations.NotNull;

@Internal(installRequired = true, uniqueInstance = true)
public interface IGoblinSystemManager extends Lifecycle {

  @NotNull
  String applicationId();

  @NotNull
  String applicationName();

  @NotNull
  RuntimeMode runtimeMode();

  @NotNull
  static IGoblinSystemManager instance() {
    IGoblinSystemManager installed = GoblinSystemManagerInstaller.INSTALLED;
    assert installed != null;
    return installed;
  }
}
