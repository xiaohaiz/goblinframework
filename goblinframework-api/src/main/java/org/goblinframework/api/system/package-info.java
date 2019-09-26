package org.goblinframework.api.system;

import static org.goblinframework.api.core.ServiceInstaller.firstOrNull;

final class GoblinSystemManagerInstaller {
  static final IGoblinSystemManager INSTALLED = firstOrNull(IGoblinSystemManager.class);
}