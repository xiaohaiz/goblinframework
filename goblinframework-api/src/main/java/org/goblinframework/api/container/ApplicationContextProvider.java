package org.goblinframework.api.container;

import org.jetbrains.annotations.NotNull;

public interface ApplicationContextProvider {

  /**
   * The return object must be instance of {@code ApplicationContext}, never null.
   */
  @NotNull
  Object applicationContext();
}
