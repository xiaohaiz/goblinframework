package org.goblinframework.core.event;

import org.goblinframework.api.concurrent.GoblinFuture;
import org.goblinframework.core.concurrent.GoblinMonoPublisher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.scheduler.Scheduler;

public class GoblinEventPublisher extends GoblinMonoPublisher<GoblinEventContext> {

  GoblinEventPublisher(@NotNull GoblinFuture<GoblinEventContext> future, @Nullable Scheduler scheduler) {
    super(future, scheduler);
  }

}
