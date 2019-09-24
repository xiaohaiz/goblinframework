package org.goblinframework.registry.zookeeper.interceptor;

import org.I0Itec.zkclient.IZkConnection;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.goblinframework.api.registry.GoblinRegistryException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;

final class ZkConnectionMethodPool {

  private static final LinkedHashMap<Method, Integer> pool;

  static {
    pool = new LinkedHashMap<>();
    try {
      initialize();
    } catch (NoSuchMethodException ex) {
      throw new GoblinRegistryException(ex);
    }
  }

  static void triggerInitialization() {
  }

  static int mode(@NotNull Method method, int defaultValue) {
    return pool.getOrDefault(method, defaultValue);
  }

  private static void initialize() throws NoSuchMethodException {
    pool.put(IZkConnection.class.getMethod("connect", Watcher.class), 0);
    pool.put(IZkConnection.class.getMethod("close"), 0);
    pool.put(IZkConnection.class.getMethod("getZookeeperState"), 0);
    pool.put(IZkConnection.class.getMethod("getServers"), 0);
    pool.put(IZkConnection.class.getMethod("multi", Iterable.class), 0);
    pool.put(IZkConnection.class.getMethod("addAuthInfo", String.class, byte[].class), 0);
    pool.put(IZkConnection.class.getMethod("create", String.class, byte[].class, CreateMode.class), 1);
    pool.put(IZkConnection.class.getMethod("create", String.class, byte[].class, List.class, CreateMode.class), 1);
    pool.put(IZkConnection.class.getMethod("delete", String.class), 2);
    pool.put(IZkConnection.class.getMethod("delete", String.class, int.class), 2);
    pool.put(IZkConnection.class.getMethod("exists", String.class, boolean.class), 2);
    pool.put(IZkConnection.class.getMethod("getChildren", String.class, boolean.class), 2);
    pool.put(IZkConnection.class.getMethod("readData", String.class, Stat.class, boolean.class), 2);
    pool.put(IZkConnection.class.getMethod("writeData", String.class, byte[].class, int.class), 2);
    pool.put(IZkConnection.class.getMethod("writeDataReturnStat", String.class, byte[].class, int.class), 2);
    pool.put(IZkConnection.class.getMethod("getCreateTime", String.class), 2);
    pool.put(IZkConnection.class.getMethod("setAcl", String.class, List.class, int.class), 2);
    pool.put(IZkConnection.class.getMethod("getAcl", String.class), 2);
  }
}
