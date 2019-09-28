package org.goblinframework.api.test;

import org.goblinframework.api.core.Internal;
import org.jetbrains.annotations.NotNull;

@Internal(uniqueInstance = true)
public interface ITestExecutionListenerManager {

  void register(@NotNull TestExecutionListener listener);

}
