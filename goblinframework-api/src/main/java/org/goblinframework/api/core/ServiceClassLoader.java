package org.goblinframework.api.core;

import org.jetbrains.annotations.NotNull;

abstract public class ServiceClassLoader {

  @NotNull
  public static ClassLoader defaultClassLoader() {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    } catch (Throwable ignore) {
    }
    if (cl == null) {
      cl = ServiceClassLoader.class.getClassLoader();
      if (cl == null) {
        try {
          cl = ClassLoader.getSystemClassLoader();
        } catch (Throwable ignore) {
        }
      }
    }
    if (cl == null) {
      throw new GoblinServiceException("Cannot access ClassLoader");
    }
    return cl;
  }

}
