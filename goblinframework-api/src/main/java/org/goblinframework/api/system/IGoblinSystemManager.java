package org.goblinframework.api.system;

import org.goblinframework.api.core.Block0;
import org.goblinframework.api.core.Internal;
import org.goblinframework.api.core.Lifecycle;
import org.jetbrains.annotations.NotNull;

@Internal(installRequired = true, uniqueInstance = true)
public interface IGoblinSystemManager extends Lifecycle {

  @NotNull
  String applicationId();

  @NotNull
  String applicationName();

  @NotNull
  RuntimeMode runtimeMode();

  void registerPriorFinalizationTask(@NotNull Block0 action);

  @NotNull
  static IGoblinSystemManager instance() {
    IGoblinSystemManager installed = GoblinSystemManagerInstaller.INSTALLED;
    assert installed != null;
    return installed;
  }
}
