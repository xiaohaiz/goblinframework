package org.goblinframework.api.management;

import static org.goblinframework.api.core.ServiceInstaller.firstOrNull;

final class ManagementServerManagerInstaller {
  static final IManagementServerManager INSTALLED = firstOrNull(IManagementServerManager.class);
}