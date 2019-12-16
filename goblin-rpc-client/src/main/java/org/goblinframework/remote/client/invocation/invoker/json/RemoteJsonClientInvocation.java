package org.goblinframework.remote.client.invocation.invoker.json;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.core.mapper.JsonMapper;
import org.goblinframework.core.util.ArrayUtils;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.remote.client.invocation.RemoteClientInvocation;
import org.goblinframework.rpc.protocol.RpcRequest;
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

  public RemoteJsonClientInvokerFuture future;

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
    this.future = new RemoteJsonClientInvokerFuture(maxTimeout);
    this.future.instruction = instruction;
  }

  @Override
  public void complete(@Nullable Object result) {
    future.complete((String) result);
  }

  @Override
  public void complete(@Nullable Object result, @Nullable Throwable cause) {
    future.complete((String) result, cause);
  }

  @NotNull
  @Override
  public RpcRequest createRequest() {
    RpcRequest request = new RpcRequest();
    request.serviceInterface = serviceId.getServiceInterface();
    request.serviceVersion = serviceId.getServiceVersion();
    request.methodName = methodName;
    request.parameterTypes = (parameterTypes == null ? ArrayUtils.EMPTY_STRING_ARRAY : parameterTypes);
    request.returnType = returnType;
    request.arguments = new Object[]{StringUtils.defaultIfBlank(arguments, JsonMapper.EMPTY_JSON_ARRAY)};
    request.timeout = 0;
    request.jsonMode = true;
    return request;
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
