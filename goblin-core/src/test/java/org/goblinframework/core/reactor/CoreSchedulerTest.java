package org.goblinframework.core.reactor;

import org.goblinframework.test.runner.GoblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.scheduler.Scheduler;

import java.util.concurrent.CountDownLatch;

@RunWith(GoblinTestRunner.class)
@ContextConfiguration("/UT.xml")
public class CoreSchedulerTest {

  @Test
  public void coreScheduler() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    Scheduler.Worker worker = CoreScheduler.getInstance().createWorker();
    worker.schedule(latch::countDown);
    latch.await();
    worker.dispose();
  }
}