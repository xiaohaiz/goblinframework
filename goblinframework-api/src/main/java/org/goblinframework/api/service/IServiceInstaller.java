package org.goblinframework.api.service;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.api.annotation.SPI;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SPI
@Internal
public interface IServiceInstaller {

  <E> List<E> asList(@NotNull Class<E> serviceType);

  <E> E firstOrNull(@NotNull Class<E> serviceType);

  static IServiceInstaller instance() {
    return null;
  }
}
