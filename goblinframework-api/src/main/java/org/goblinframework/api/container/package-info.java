package org.goblinframework.api.container;

import org.goblinframework.api.service.ServiceInstaller;

final class SpringContainerManagerInstaller {
  static final ISpringContainerManager INSTALLED = ServiceInstaller.firstOrNull(ISpringContainerManager.class);
}