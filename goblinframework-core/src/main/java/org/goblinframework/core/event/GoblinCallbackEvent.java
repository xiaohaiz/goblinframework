package org.goblinframework.core.event;

import org.goblinframework.api.function.GoblinCallback;
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
