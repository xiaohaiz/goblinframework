package org.goblinframework.api.event;

import org.goblinframework.api.annotation.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
public interface IEventBusManager {

  @NotNull
  static IEventBusManager instance() {
    IEventBusManager manager = EventBusManagerInstaller.INSTALLED;
    if (manager == null) {
      throw new GoblinEventException("No IEventBusManager installed");
    }
    return manager;
  }
}
