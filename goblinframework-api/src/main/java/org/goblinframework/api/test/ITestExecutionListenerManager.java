package org.goblinframework.api.test;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.api.annotation.SPI;
import org.goblinframework.api.service.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SPI
@Internal
public interface ITestExecutionListenerManager {

  void register(@NotNull TestExecutionListener listener);

  @Nullable
  static ITestExecutionListenerManager instance() {
    return ServiceInstaller.firstOrNull(ITestExecutionListenerManager.class);
  }
}
