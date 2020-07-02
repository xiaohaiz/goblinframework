package org.goblinframework.remote.client.module.runtime;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedObject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@GoblinManagedBean(type = "RemoteClient")
public class RemoteServiceMethodInformation extends GoblinManagedObject
    implements RemoteServiceMethodInformationMXBean {

  @NotNull private final RemoteServiceInformation serviceInformation;
  @NotNull private final Method method;

  RemoteServiceMethodInformation(@NotNull RemoteServiceInformation serviceInformation, @NotNull Method method) {
    this.serviceInformation = serviceInformation;
    this.method = method;
  }

  Long timeout;
  Integer retries;
  SerializerMode encoder;
  boolean asynchronous;
  boolean noResponseWait;
  boolean dispatchAll;
  boolean ignoreNoProvider;

  @NotNull
  @Override
  public String getMethodName() {
    return method.getName();
  }

  @NotNull
  @Override
  public String getReturnType() {
    return method.getReturnType().getName();
  }

  @Override
  public long getTimeout() {
    return timeout != null ? timeout : serviceInformation.timeout;
  }

  @Override
  public int getRetries() {
    if (asynchronous || noResponseWait) {
      return 0;
    }
    return retries != null ? retries : serviceInformation.retries;
  }

  @NotNull
  @Override
  public SerializerMode getEncoder() {
    return encoder != null ? encoder : serviceInformation.encoder;
  }

  @Override
  public boolean getAsynchronous() {
    return asynchronous;
  }

  @Override
  public boolean getNoResponseWait() {
    return noResponseWait;
  }

  @Override
  public boolean getDispatchAll() {
    return dispatchAll;
  }

  @Override
  public boolean getIgnoreNoProvider() {
    return ignoreNoProvider;
  }
}
