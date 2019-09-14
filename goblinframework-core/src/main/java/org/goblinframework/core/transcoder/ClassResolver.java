package org.goblinframework.core.transcoder;

import org.jetbrains.annotations.NotNull;

public interface ClassResolver {

  @NotNull
  Class<?> resolve(@NotNull String className) throws ClassNotFoundException;

}
