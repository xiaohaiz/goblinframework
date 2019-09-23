package org.goblinframework.api.test;

import static org.goblinframework.api.service.ServiceInstaller.firstOrNull;

final class TestExecutionListenerManagerInstaller {
  static final ITestExecutionListenerManager INSTALLED = firstOrNull(ITestExecutionListenerManager.class);
}