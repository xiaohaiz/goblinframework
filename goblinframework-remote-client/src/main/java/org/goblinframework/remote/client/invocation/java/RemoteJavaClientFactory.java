package org.goblinframework.remote.client.invocation.java;

import org.goblinframework.api.annotation.ThreadSafe;
import org.jetbrains.annotations.NotNull;

@ThreadSafe
final public class RemoteJavaClientFactory {

  @NotNull
  public static <E> E createJavaClient() {
    throw new UnsupportedOperationException();
  }
}
