package org.goblinframework.api.monitor;

import org.jetbrains.annotations.NotNull;

public interface FlightAttribute {

  void setAttribute(@NotNull String name, @NotNull Object value);

}
