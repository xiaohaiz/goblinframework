package org.goblinframework.remote.server.service;

import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedLogger;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.remote.core.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

@GoblinManagedBean(type = "RemoteServer")
@GoblinManagedLogger(name = "goblin.remote.server.service")
abstract public class RemoteService extends GoblinManagedObject
    implements RemoteServiceMXBean {

  @NotNull private final RemoteServiceId serviceId;

  protected RemoteService(@NotNull RemoteServiceId serviceId) {
    this.serviceId = serviceId;
  }

  @NotNull
  abstract public Class<?> getServiceType();

  @NotNull
  abstract public Object getServiceBean();

  @Nullable
  public Object invoke(@NotNull Method method, @NotNull Object[] arguments) throws Throwable {
    return ReflectionUtils.invoke(getServiceBean(), method, arguments);
  }

  @NotNull
  @Override
  public String getServiceInterface() {
    return serviceId.getServiceInterface();
  }

  @NotNull
  @Override
  public String getServiceVersion() {
    return serviceId.getServiceVersion();
  }
}
