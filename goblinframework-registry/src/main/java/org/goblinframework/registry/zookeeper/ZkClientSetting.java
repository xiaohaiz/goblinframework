package org.goblinframework.registry.zookeeper;

import org.goblinframework.core.serialization.SerializerMode;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class ZkClientSetting {

  @NotNull private final String addresses;
  private final int connectionTimeout;
  private final int sessionTimeout;
  @NotNull private final SerializerMode serializer;

  private ZkClientSetting(@NotNull ZkClientSettingBuilder builder) {
    this.addresses = Objects.requireNonNull(builder.addresses);
    this.connectionTimeout = builder.connectionTimeout;
    this.sessionTimeout = builder.sessionTimeout;
    this.serializer = builder.serializer;
  }

  @NotNull
  public ZookeeperClient createZookeeperClient() {
    ZookeeperClientConfig config = new ZookeeperClientConfig(addresses, connectionTimeout, sessionTimeout, serializer);
    return ZookeeperClientBuffer.INSTANCE.create(config);
  }

  @NotNull
  public static ZkClientSettingBuilder builder() {
    return new ZkClientSettingBuilder();
  }

  final public static class ZkClientSettingBuilder {

    private String addresses;
    private int connectionTimeout = Integer.MAX_VALUE;
    private int sessionTimeout = 30000;
    private SerializerMode serializer = SerializerMode.JAVA;

    private ZkClientSettingBuilder() {
    }

    @NotNull
    public ZkClientSettingBuilder addresses(@NotNull String addresses) {
      this.addresses = StringUtils.formalizeServers(addresses, ",", () -> 2181);
      return this;
    }

    @NotNull
    public ZkClientSettingBuilder connectionTimeout(int connectionTimeout) {
      if (connectionTimeout > 0) {
        this.connectionTimeout = connectionTimeout;
      }
      return this;
    }

    @NotNull
    public ZkClientSettingBuilder sessionTimeout(int sessionTimeout) {
      if (sessionTimeout >= 0) {
        this.sessionTimeout = sessionTimeout;
      }
      return this;
    }

    @NotNull
    public ZkClientSettingBuilder serializer(@NotNull SerializerMode serializer) {
      this.serializer = serializer;
      return this;
    }

    @NotNull
    public ZkClientSetting build() {
      return new ZkClientSetting(this);
    }
  }
}
