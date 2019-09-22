package org.goblinframework.api.service;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.api.annotation.SPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SPI
@Internal
public interface IServiceInstaller {

  @NotNull
  <E> List<E> asList(@NotNull Class<E> serviceType);

  @Nullable
  <E> E firstOrNull(@NotNull Class<E> serviceType);

  @NotNull
  static IServiceInstaller instance() {
    IServiceInstaller installer = ServiceInstallerLoader.installer;
    if (installer == null) {
      throw new GoblinServiceException("No IServiceInstaller installed");
    }
    return installer;
  }
}
