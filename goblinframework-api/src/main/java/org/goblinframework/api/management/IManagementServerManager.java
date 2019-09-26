package org.goblinframework.api.management;

import org.goblinframework.api.core.Internal;
import org.goblinframework.api.core.Lifecycle;
import org.jetbrains.annotations.Nullable;

@Internal(uniqueInstance = true)
public interface IManagementServerManager extends Lifecycle {

  @Nullable
  static IManagementServerManager instance() {
    return ManagementServerManagerInstaller.INSTALLED;
  }
}
