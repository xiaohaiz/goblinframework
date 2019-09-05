package org.goblinframework.core.event;

import org.jetbrains.annotations.NotNull;

public interface GoblinEventContext {

  @NotNull
  String getChannel();

  @NotNull
  GoblinEvent getEvent();

}
