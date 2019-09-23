package org.goblinframework.api.management;

import static org.goblinframework.api.service.ServiceInstaller.firstOrNull;

final class ManagementControllerManagerInstaller {
  static final IManagementControllerManager INSTALLED = firstOrNull(IManagementControllerManager.class);
}

final class ManagementServerManagerInstaller {
  static final IManagementServerManager INSTALLED = firstOrNull(IManagementServerManager.class);
}