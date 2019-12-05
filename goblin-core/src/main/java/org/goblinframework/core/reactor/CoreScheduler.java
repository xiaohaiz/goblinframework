package org.goblinframework.core.reactor;

import org.goblinframework.core.util.NamedDaemonThreadFactory;
import org.goblinframework.core.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ThreadFactory;

final public class CoreScheduler {
  private static final Logger logger = LoggerFactory.getLogger(CoreScheduler.class);

  private static final Scheduler scheduler;

  static {
    int threadCap = SystemUtils.availableProcessors() * 10;
    int queuedTaskCap = 102400;
    ThreadFactory threadFactory = NamedDaemonThreadFactory.getInstance("CoreScheduler");
    int ttlSeconds = 60;
    scheduler = Schedulers.newBoundedElastic(threadCap, queuedTaskCap, threadFactory, ttlSeconds);
    logger.debug("{CoreScheduler} Core scheduler initialized [threadCap={},queuedTaskCap={},ttlSeconds={}]",
        threadCap, queuedTaskCap, ttlSeconds);
  }

  public static Scheduler getInstance() {
    return scheduler;
  }

  private CoreScheduler() {
  }

  public static void initialize() {
  }

  public static void dispose() {
    scheduler.dispose();
    logger.debug("{CoreScheduler} Core scheduler disposed");
  }
}
