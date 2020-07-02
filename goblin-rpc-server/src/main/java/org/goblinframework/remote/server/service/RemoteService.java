package org.goblinframework.remote.server.service;

import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedLogger;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.system.GoblinSystem;
import org.goblinframework.core.util.HostAndPort;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.registry.zookeeper.ZookeeperRegistryPathKeeper;
import org.goblinframework.remote.server.module.config.RemoteServerConfig;
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager;
import org.goblinframework.rpc.registry.RpcServerNode;
import org.goblinframework.rpc.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@GoblinManagedBean(type = "RemoteServer")
@GoblinManagedLogger(name = "goblin.remote.server.service")
abstract public class RemoteService extends GoblinManagedObject
    implements RemoteServiceMXBean {

  @NotNull private final RemoteServiceId serviceId;
  @NotNull private final List<String> servicePathList;
  @Nullable private ZookeeperRegistryPathKeeper keeper;

  protected RemoteService(@NotNull RemoteServiceId serviceId,
                          @NotNull List<HostAndPort> serverAddresses) {
    this.serviceId = serviceId;
    this.servicePathList = generateServicePathList(serverAddresses);
  }

  synchronized void initialize(@Nullable ZookeeperRegistryPathKeeper keeper) {
    this.keeper = keeper;
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

  private List<String> generateServicePathList(List<HostAndPort> serverAddresses) {
    if (serverAddresses.isEmpty()) {
      return Collections.emptyList();
    }
    RemoteServerConfig serverConfig = RemoteServerConfigManager.INSTANCE.getRemoteServerConfig();
    List<String> servicePathList = new ArrayList<>();
    for (HostAndPort address : serverAddresses) {
      RpcServerNode node = new RpcServerNode();
      node.setServerId(GoblinSystem.applicationId());
      node.setServerName(GoblinSystem.applicationName());
      node.setServerHost(address.host);
      node.setServerPort(address.port);
      node.setServerVersion(serviceId.getServiceVersion());
      node.setServerDomain(GoblinSystem.runtimeMode().name());
      node.setServerWeight(serverConfig.getWeight());
      String path = node.toPath();
      servicePathList.add("/goblin/remote/service/" + serviceId.getServiceInterface() + "/server/" + path);
    }
    return servicePathList;
  }

  synchronized void publish() {
    if (keeper == null) {
      return;
    }
    for (String path : servicePathList) {
      keeper.watch(path, true, null);
    }
  }

  synchronized void unregister() {
    if (keeper == null) {
      return;
    }
    for (String path : servicePathList) {
      keeper.unwatch(path);
    }
  }
}
