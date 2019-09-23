package org.goblinframework.api.config;

import static org.goblinframework.api.service.ServiceInstaller.firstOrNull;

final class ConfigParserManagerInstaller {
  static final IConfigParserManager INSTALLED = firstOrNull(IConfigParserManager.class);
}