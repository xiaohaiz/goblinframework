package org.goblinframework.schedule.support;

public class GoblinTimerEventGenerator implements org.goblinframework.core.module.spi.GoblinTimerEventGenerator {

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  @Override
  public boolean isRunning() {
    return false;
  }
}
