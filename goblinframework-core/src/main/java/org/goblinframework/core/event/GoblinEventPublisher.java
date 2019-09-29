package org.goblinframework.core.event;

import org.goblinframework.core.concurrent.GoblinMonoPublisher;
import org.jetbrains.annotations.Nullable;
import reactor.core.scheduler.Scheduler;

public class GoblinEventPublisher extends GoblinMonoPublisher<GoblinEventContext> {

  GoblinEventPublisher(@Nullable Scheduler scheduler) {
    super(scheduler);
  }

}
