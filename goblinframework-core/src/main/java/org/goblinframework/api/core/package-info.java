package org.goblinframework.api.core;

final class SpringContainerManagerInstaller {
  static final ISpringContainerManager INSTALLED = ServiceInstaller.firstOrNull(ISpringContainerManager.class);
}