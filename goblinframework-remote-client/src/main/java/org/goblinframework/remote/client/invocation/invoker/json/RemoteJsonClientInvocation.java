package org.goblinframework.remote.client.invocation.invoker.json;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.remote.client.invocation.RemoteClientInvocation;
import org.goblinframework.remote.core.protocol.RemoteRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemoteJsonClientInvocation extends RemoteClientInvocation {

  public String methodName;
  public String[] parameterTypes;
  public String returnType;
  public String arguments;
  public SerializerMode encoder;
  public long timeout;
  public boolean noResponseWait;
  public boolean dispatchAll;
  public boolean ignoreNoProvider;

  @Override
  public SerializerMode encoder() {
    return encoder;
  }

  @Override
  public long timeout() {
    return timeout;
  }

  @Override
  public boolean asynchronous() {
    return false;
  }

  @Override
  public boolean noResponseWait() {
    return noResponseWait;
  }

  @Override
  public boolean dispatchAll() {
    return dispatchAll;
  }

  @Override
  public boolean ignoreNoProvider() {
    return ignoreNoProvider;
  }

  @Override
  public void initializeFuture() {

  }

  @Override
  public void complete(@Nullable Object result) {

  }

  @Override
  public void complete(@Nullable Object result, @Nullable Throwable cause) {

  }

  @NotNull
  @Override
  public RemoteRequest createRequest() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String asMethodText() {
    StringBuilder sbuf = new StringBuilder();
    if (parameterTypes != null && parameterTypes.length > 0) {
      for (String parameterType : parameterTypes) {
        sbuf.append(parameterType);
        sbuf.append(",");
      }
    }
    if (sbuf.length() > 0) {
      sbuf.setLength(sbuf.length() - 1);
    }
    String text = methodName + "(" + sbuf.toString() + ")";
    if (returnType != null) {
      text = returnType + " " + text;
    }
    return text;
  }
}
