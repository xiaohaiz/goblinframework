package org.goblinframework.remote.client.invocation.invoker.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.goblinframework.api.concurrent.GoblinFuture;
import org.goblinframework.core.concurrent.GoblinFutureImpl;
import org.goblinframework.core.concurrent.GoblinInterruptedException;
import org.goblinframework.core.concurrent.GoblinTimeoutException;
import org.goblinframework.core.exception.GoblinMappingException;
import org.goblinframework.core.mapper.JsonMapper;
import org.goblinframework.remote.client.module.exception.ClientInvocationException;
import org.goblinframework.remote.client.module.monitor.RIC;
import org.goblinframework.rpc.exception.RpcException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

final public class RemoteJsonClientInvokerFuture extends GoblinFutureImpl<String> {

  private final int maxTimeout;
  @Nullable RIC instruction;

  RemoteJsonClientInvokerFuture(int maxTimeout) {
    this.maxTimeout = maxTimeout;
  }

  @Override
  public String get() {
    try {
      return get(maxTimeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (TimeoutException ex) {
      throw new GoblinTimeoutException(ex);
    } catch (ExecutionException ex) {
      Throwable cause = ex.getCause();
      if (cause instanceof RpcException) {
        throw (RpcException) cause;
      }
      throw new ClientInvocationException(cause);
    }
  }

  @Override
  public GoblinFuture<String> complete(String result) {
    return complete(result, null);
  }

  @Override
  public GoblinFuture<String> complete(String result, Throwable cause) {
    if (instruction != null) {
      instruction.complete();
    }
    return super.complete(result, cause);
  }

  public <T> T mapAs(@NotNull JavaType type) {
    String json = get();
    if (json == null) {
      return null;
    }
    try {
      return JsonMapper.getDefaultObjectMapper().readValue(json, type);
    } catch (IOException ex) {
      throw new GoblinMappingException(ex);
    }
  }

  public <T> T mapAsObject(@NotNull Class<T> type) {
    String json = get();
    if (json == null) {
      return null;
    }
    return JsonMapper.asObject(json, type);
  }

  public <T> List<T> mapAsList(@NotNull Class<?> elementType) {
    TypeFactory typeFactory = JsonMapper.getDefaultObjectMapper().getTypeFactory();
    JavaType type = typeFactory.constructCollectionType(LinkedList.class, elementType);
    return mapAs(type);
  }
}
