package org.goblinframework.core.schedule;

import org.jetbrains.annotations.NotNull;

public interface CronTask {

  @NotNull
  String name();

  @NotNull
  String cronExpression();

  boolean concurrent();

  boolean flight();

  void execute();
}
