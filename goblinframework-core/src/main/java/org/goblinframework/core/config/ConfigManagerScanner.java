package org.goblinframework.core.config;

import org.goblinframework.api.core.Disposable;
import org.goblinframework.api.core.Initializable;
import org.goblinframework.api.schedule.CronConstants;
import org.goblinframework.api.schedule.CronTask;
import org.goblinframework.api.schedule.ICronTaskManager;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Config modification detector. Check if config changed or not every one minute.
 */
final class ConfigManagerScanner implements Initializable, Disposable {
  private static final String TASK_NAME = "ConfigManagerScanner";

  private final ConfigManager configManager;
  private final AtomicReference<Timer> timer = new AtomicReference<>();

  ConfigManagerScanner(@NotNull ConfigManager configManager) {
    this.configManager = configManager;
  }

  @Override
  public void initialize() {
    ICronTaskManager cronTaskManager = ICronTaskManager.instance();
    if (cronTaskManager != null) {
      cronTaskManager.register(new CronTask() {
        @NotNull
        @Override
        public String name() {
          return TASK_NAME;
        }

        @NotNull
        @Override
        public String cronExpression() {
          return CronConstants.MINUTE_TIMER;
        }

        @Override
        public boolean concurrent() {
          return false;
        }

        @Override
        public boolean flight() {
          return false;
        }

        @Override
        public void execute() {
          configManager.reload();
        }
      });
    } else {
      Timer timer = new Timer(TASK_NAME, true);
      timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          synchronized (ConfigManagerScanner.this) {
            configManager.reload();
          }
        }
      }, 60000, 60000);
      this.timer.set(timer);
    }
  }

  @Override
  public void dispose() {
    Timer timer = this.timer.getAndSet(null);
    if (timer != null) {
      timer.cancel();
    }
  }
}
