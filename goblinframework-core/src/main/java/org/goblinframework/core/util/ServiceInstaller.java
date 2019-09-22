package org.goblinframework.core.util;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.common.Ordered;
import org.goblinframework.api.service.IServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

abstract public class ServiceInstaller {

  @NotNull
  public static <E> List<E> installedList(@NotNull Class<E> serviceType) {
    return ServiceLoaderImpl.INSTANCE.asList(serviceType);
  }

  @Nullable
  public static <E> E installedFirst(@NotNull Class<E> serviceType) {
    return ServiceLoaderImpl.INSTANCE.firstOrNull(serviceType);
  }

  @Install
  final public static class Installer implements IServiceInstaller, Ordered {

    @Override
    public int getOrder() {
      return HIGHEST_PRECEDENCE;
    }

    @NotNull
    @Override
    public <E> List<E> asList(@NotNull Class<E> serviceType) {
      return ServiceLoaderImpl.INSTANCE.asList(serviceType);
    }

    @Nullable
    @Override
    public <E> E firstOrNull(@NotNull Class<E> serviceType) {
      return ServiceLoaderImpl.INSTANCE.firstOrNull(serviceType);
    }
  }
}
