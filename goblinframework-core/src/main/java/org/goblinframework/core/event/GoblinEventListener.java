package org.goblinframework.core.event;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface GoblinEventListener extends EventListener {

  boolean accept(@NotNull GoblinEventContext context);

  void onEvent(@NotNull GoblinEventContext context);

}
