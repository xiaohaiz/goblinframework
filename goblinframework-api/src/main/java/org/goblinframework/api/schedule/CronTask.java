package org.goblinframework.api.schedule;

import org.goblinframework.api.common.Block1;
import org.goblinframework.api.monitor.FlightAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CronTask {

  @NotNull
  String name();

  @NotNull
  String cronExpression();

  boolean concurrent();

  @Nullable
  default Block1<FlightAttribute> flightAttribute() {
    return null;
  }

  void execute();
}
