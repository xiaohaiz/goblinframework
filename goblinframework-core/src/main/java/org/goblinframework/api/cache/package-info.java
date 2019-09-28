package org.goblinframework.api.cache;

import org.goblinframework.core.service.ServiceInstaller;
import org.jetbrains.annotations.Nullable;

final class CacheBuilderManagerInstaller {

  @Nullable static final ICacheBuilderManager installed;

  static {
    installed = ServiceInstaller.firstOrNull(ICacheBuilderManager.class);
  }
}