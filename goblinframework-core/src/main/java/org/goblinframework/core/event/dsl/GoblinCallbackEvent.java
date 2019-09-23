package org.goblinframework.core.event.dsl;

import org.goblinframework.api.event.GoblinCallback;
import org.goblinframework.api.event.GoblinEvent;
import org.goblinframework.api.event.GoblinEventChannel;
import org.jetbrains.annotations.NotNull;

@GoblinEventChannel("/goblin/core")
final public class GoblinCallbackEvent extends GoblinEvent {

  private final GoblinCallback<?> callback;

  public GoblinCallbackEvent(@NotNull GoblinCallback<?> callback) {
    this.callback = callback;
    setRaiseException(true);
  }

  @NotNull
  public GoblinCallback<?> getCallback() {
    return callback;
  }
}
