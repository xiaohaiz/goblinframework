package org.goblinframework.core.event.timer;

import org.goblinframework.api.common.Disposable;
import org.goblinframework.api.common.Initializable;
import org.goblinframework.api.schedule.ICronTaskManager;

import java.util.*;

final public class TimerEventGenerator implements Initializable, Disposable {

  private final List<Timer> timers = Collections.synchronizedList(new ArrayList<>());

  @Override
  public void initialize() {
    ICronTaskManager cronTaskManager = ICronTaskManager.instance();
    if (cronTaskManager != null) {
      cronTaskManager.register(SecondTimerEventGenerator.INSTANCE);
      cronTaskManager.register(MinuteTimerEventGenerator.INSTANCE);
    } else {
      Timer secondTimer = new Timer("SecondTimerEventGenerator", true);
      secondTimer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          SecondTimerEventGenerator.INSTANCE.execute();
        }
      }, 0, 1000);
      timers.add(secondTimer);
      Timer minuteTimer = new Timer("MinuteTimerEventGenerator", true);
      secondTimer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          MinuteTimerEventGenerator.INSTANCE.execute();
        }
      }, 0, 60000);
      timers.add(minuteTimer);
    }
  }

  @Override
  public void dispose() {
    timers.parallelStream().forEach(Timer::cancel);
    timers.clear();
  }
}
