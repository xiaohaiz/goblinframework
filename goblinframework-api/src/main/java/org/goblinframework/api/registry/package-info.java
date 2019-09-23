package org.goblinframework.api.registry;

import static org.goblinframework.api.service.ServiceInstaller.firstOrNull;

final class RegistryBuilderManagerInstaller {
  static final IRegistryBuilderManager INSTALLED = firstOrNull(IRegistryBuilderManager.class);
}