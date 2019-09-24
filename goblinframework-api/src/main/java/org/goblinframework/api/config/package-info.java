package org.goblinframework.api.config;

import org.goblinframework.api.service.ServiceInstaller;

final class ConfigManagerInstaller {
  static final IConfigManager INSTALLED = ServiceInstaller.firstOrNull(IConfigManager.class);
}