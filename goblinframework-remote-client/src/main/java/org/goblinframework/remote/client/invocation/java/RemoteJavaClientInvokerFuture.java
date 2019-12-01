package org.goblinframework.remote.client.invocation.java;

import org.goblinframework.api.concurrent.GoblinFuture;
import org.goblinframework.core.concurrent.GoblinFutureImpl;
import org.goblinframework.core.concurrent.GoblinInterruptedException;
import org.goblinframework.core.concurrent.GoblinTimeoutException;
import org.goblinframework.remote.client.module.exception.ClientInvocationException;
import org.goblinframework.remote.client.module.monitor.RIC;
import org.goblinframework.remote.core.module.exception.RemoteException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RemoteJavaClientInvokerFuture extends GoblinFutureImpl<Object> {

  private final int maxTimeout;
  @Nullable RIC instruction;

  public RemoteJavaClientInvokerFuture(int maxTimeout) {
    this.maxTimeout = maxTimeout;
  }

  @Override
  public Object get() {
    try {
      return get(maxTimeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (TimeoutException ex) {
      throw new GoblinTimeoutException(ex);
    } catch (ExecutionException ex) {
      Throwable cause = ex.getCause();
      if (cause instanceof RemoteException) {
        throw (RemoteException) cause;
      }
      throw new ClientInvocationException(cause);
    }
  }

  @Override
  public GoblinFuture<Object> complete(Object result) {
    return complete(result, null);
  }

  @Override
  public GoblinFuture<Object> complete(Object result, Throwable cause) {
    if (instruction != null) {
      instruction.complete();
    }
    return super.complete(result, cause);
  }

  @NotNull
  public RemoteJavaClientInvokerPublisher asPublisher() {
    return new RemoteJavaClientInvokerPublisher(this);
  }
}
