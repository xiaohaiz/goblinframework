package org.goblinframework.registry.zookeeper;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class ZookeeperClientSetting {

  @NotNull private final String addresses;
  private final int connectionTimeout;
  private final int sessionTimeout;
  @NotNull private final SerializerMode serializer;

  private ZookeeperClientSetting(@NotNull Builder builder) {
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
  public static Builder builder() {
    return new Builder();
  }

  final public static class Builder {

    private String addresses;
    private int connectionTimeout = Integer.MAX_VALUE;
    private int sessionTimeout = 30000;
    private SerializerMode serializer = SerializerMode.JAVA;

    private Builder() {
    }

    @NotNull
    public Builder addresses(@NotNull String addresses) {
      this.addresses = StringUtils.formalizeServers(addresses, ",", () -> 2181);
      return this;
    }

    @NotNull
    public Builder connectionTimeout(int connectionTimeout) {
      if (connectionTimeout > 0) {
        this.connectionTimeout = connectionTimeout;
      }
      return this;
    }

    @NotNull
    public Builder sessionTimeout(int sessionTimeout) {
      if (sessionTimeout >= 0) {
        this.sessionTimeout = sessionTimeout;
      }
      return this;
    }

    @NotNull
    public Builder serializer(@NotNull SerializerMode serializer) {
      this.serializer = serializer;
      return this;
    }

    @NotNull
    public ZookeeperClientSetting build() {
      return new ZookeeperClientSetting(this);
    }
  }
}
