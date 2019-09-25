package org.goblinframework.api.schedule;

import org.jetbrains.annotations.NotNull;

public interface CronTask {

  @NotNull
  String name();

  @NotNull
  String cronExpression();

  boolean concurrent();

  default boolean flightSilence() {
    return false;
  }

  void execute();
}
