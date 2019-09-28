package org.goblinframework.api.registry;

import static org.goblinframework.core.service.ServiceInstaller.firstOrNull;

final class RegistryBuilderManagerInstaller {
  static final IRegistryBuilderManager INSTALLED = firstOrNull(IRegistryBuilderManager.class);
}