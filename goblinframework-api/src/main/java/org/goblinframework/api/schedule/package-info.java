package org.goblinframework.api.schedule;

import org.goblinframework.api.core.ServiceInstaller;

final class CronTaskManagerInstaller {
  static final ICronTaskManager INSTALLED = ServiceInstaller.firstOrNull(ICronTaskManager.class);
}