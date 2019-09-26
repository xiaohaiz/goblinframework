package org.goblinframework.api.test;

import org.goblinframework.api.core.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal(uniqueInstance = true)
public interface ITestExecutionListenerManager {

  void register(@NotNull TestExecutionListener listener);

  @Nullable
  static ITestExecutionListenerManager instance() {
    return TestExecutionListenerManagerInstaller.INSTALLED;
  }
}
