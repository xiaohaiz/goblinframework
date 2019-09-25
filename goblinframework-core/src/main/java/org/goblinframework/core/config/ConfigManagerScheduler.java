package org.goblinframework.core.config;

import org.goblinframework.api.common.Disposable;
import org.goblinframework.api.common.Initializable;
import org.goblinframework.api.schedule.CronConstants;
import org.goblinframework.api.schedule.CronTask;
import org.goblinframework.api.schedule.ICronTaskManager;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

final class ConfigManagerScheduler implements Initializable, Disposable {
  private static final String TASK_NAME = "ConfigManagerScheduler";

  private final ConfigManager configManager;
  private final AtomicReference<ICronTaskManager> cronTaskManager = new AtomicReference<>();
  private final AtomicReference<Timer> timer = new AtomicReference<>();

  ConfigManagerScheduler(@NotNull ConfigManager configManager) {
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
        public void execute() {
          configManager.reload();
        }
      });
      this.cronTaskManager.set(cronTaskManager);
    } else {
      Timer timer = new Timer(TASK_NAME, true);
      timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          configManager.reload();
        }
      }, 60000, 60000);
      this.timer.set(timer);
    }
  }

  @Override
  public void dispose() {
    ICronTaskManager cronTaskManager = this.cronTaskManager.getAndSet(null);
    if (cronTaskManager != null) {
      cronTaskManager.unregister(TASK_NAME);
    }
    Timer timer = this.timer.getAndSet(null);
    if (timer != null) {
      timer.cancel();
    }
  }
}
