package org.goblinframework.remote.client.module.runtime;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.remote.core.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

@GoblinManagedBean(type = "RemoteClient")
public class RemoteServiceInformation extends GoblinManagedObject
    implements RemoteServiceInformationMXBean {

  Class<?> interfaceClass;
  String version;
  long timeout;
  int retries;
  SerializerMode encoder;
  final Map<Method, RemoteServiceMethodInformation> methodInformationMap = new LinkedHashMap<>();

  @NotNull
  public RemoteServiceId createId(@Nullable String specifiedVersion) {
    String version = StringUtils.defaultString(specifiedVersion, getServiceVersion());
    return new RemoteServiceId(getServiceInterface(), version);
  }

  @Nullable
  public RemoteServiceMethodInformation getRemoteServiceMethodInformation(@NotNull Method method) {
    return methodInformationMap.get(method);
  }

  @NotNull
  @Override
  public String getServiceInterface() {
    return interfaceClass.getName();
  }

  @NotNull
  @Override
  public String getServiceVersion() {
    return version;
  }

  @Override
  public long getServiceTimeout() {
    return timeout;
  }

  @Override
  public int getServiceRetries() {
    return retries;
  }


  @NotNull
  @Override
  public SerializerMode getServiceEncoder() {
    return encoder;
  }

  @NotNull
  @Override
  public RemoteServiceMethodInformationMXBean[] getRemoteServiceMethodInformationList() {
    return methodInformationMap.values().toArray(new RemoteServiceMethodInformationMXBean[0]);
  }

  @Override
  protected void disposeBean() {
    methodInformationMap.values().forEach(RemoteServiceMethodInformation::dispose);
    methodInformationMap.clear();
  }
}
