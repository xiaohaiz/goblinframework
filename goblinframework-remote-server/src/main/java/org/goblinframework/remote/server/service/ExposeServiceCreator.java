package org.goblinframework.remote.server.service;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExposeServiceCreator {

  public static void create(@NotNull Object... beans) {
    for (Object bean : beans) {
      List<ExposeServiceId> ids = ExposeServiceIdGenerator.generate(bean.getClass());
      if (ids.isEmpty()) {
        continue;
      }
      RemoteServiceManager.INSTANCE.createStaticService(bean, ids);
    }
  }

}
