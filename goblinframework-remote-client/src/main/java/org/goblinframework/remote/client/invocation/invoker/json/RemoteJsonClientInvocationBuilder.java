package org.goblinframework.remote.client.invocation.invoker.json;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.core.mapper.JsonMapper;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.remote.client.module.config.RemoteClientConfig;
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager;
import org.goblinframework.remote.client.module.exception.ClientInvocationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;

final public class RemoteJsonClientInvocationBuilder {

  @NotNull private final RemoteJsonClient jsonClient;
  private String methodName;
  private final List<String> parameterTypes = new ArrayList<>();
  private String returnType;
  private final List<Object> arguments = new ArrayList<>();
  private SerializerMode encoder;
  private long timeout;
  private boolean noResponseWait;
  private boolean dispatchAll;
  private boolean ignoreNoProvider;

  RemoteJsonClientInvocationBuilder(@NotNull RemoteJsonClient jsonClient) {
    this.jsonClient = jsonClient;
  }

  @NotNull
  public RemoteJsonClientInvocationBuilder method(@NotNull String method) {
    this.methodName = method;
    return this;
  }

  @NotNull
  public RemoteJsonClientInvocationBuilder parameterTypes(@Nullable String... parameterTypes) {
    if (parameterTypes != null) {
      Arrays.stream(parameterTypes).filter(Objects::nonNull).forEach(this.parameterTypes::add);
    }
    return this;
  }

  @NotNull
  public RemoteJsonClientInvocationBuilder returnType(@Nullable String returnType) {
    this.returnType = returnType;
    return this;
  }

  @NotNull
  public RemoteJsonClientInvocationBuilder arguments(@Nullable Object... arguments) {
    if (arguments != null) {
      Collections.addAll(this.arguments, arguments);
    }
    return this;
  }

  @NotNull
  public RemoteJsonClientInvocationBuilder encoder(@Nullable SerializerMode encoder) {
    this.encoder = encoder;
    return this;
  }

  @NotNull
  public RemoteJsonClientInvocationBuilder timeout(long timeout, @NotNull TimeUnit unit) {
    this.timeout = unit.toMillis(timeout);
    return this;
  }

  @NotNull
  public RemoteJsonClientInvocationBuilder noResponseWait(boolean noResponseWait) {
    this.noResponseWait = noResponseWait;
    return this;
  }

  @NotNull
  public RemoteJsonClientInvocationBuilder dispatchAll(boolean dispatchAll) {
    this.dispatchAll = dispatchAll;
    return this;
  }

  @NotNull
  public RemoteJsonClientInvocationBuilder ignoreNoProvider(boolean ignoreNoProvider) {
    this.ignoreNoProvider = ignoreNoProvider;
    return this;
  }

  @NotNull
  public RemoteJsonClientInvokerFuture invoke() {
    if (StringUtils.isBlank(methodName)) {
      throw new ClientInvocationException("methodName is required");
    }
    long timeout = this.timeout;
    if (timeout <= 0) {
      RemoteClientConfig clientConfig = RemoteClientConfigManager.INSTANCE.getRemoteClientConfig();
      timeout = clientConfig.getMaxTimeout();
    }

    RemoteJsonClientInvocation invocation = new RemoteJsonClientInvocation();
    invocation.serviceId = jsonClient.serviceId;
    invocation.methodName = methodName;
    invocation.parameterTypes = parameterTypes.toArray(new String[0]);
    invocation.returnType = returnType;
    try {
      invocation.arguments = JsonMapper.toJson(arguments);
    } catch (Exception ex) {
      throw new ClientInvocationException(ex);
    }
    invocation.encoder = encoder;
    invocation.timeout = timeout;
    invocation.noResponseWait = noResponseWait;
    invocation.dispatchAll = dispatchAll;
    invocation.ignoreNoProvider = ignoreNoProvider;
    return jsonClient.invoke(invocation);
  }
}
