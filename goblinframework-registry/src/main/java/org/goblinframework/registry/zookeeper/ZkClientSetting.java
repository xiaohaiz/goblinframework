package org.goblinframework.registry.zookeeper;

import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerManager;
import org.goblinframework.core.serialization.SerializerMode;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class ZkClientSetting {

  @NotNull private final String addresses;
  private final int connectionTimeout;
  private final int sessionTimeout;
  @NotNull private final Serializer serializer;

  private ZkClientSetting(@NotNull ZkClientSettingBuilder builder) {
    this.addresses = Objects.requireNonNull(builder.addresses);
    this.connectionTimeout = builder.connectionTimeout;
    this.sessionTimeout = builder.sessionTimeout;
    this.serializer = SerializerManager.INSTANCE.getSerializer(builder.serializer);
  }

  @NotNull
  public String addresses() {
    return addresses;
  }

  public int connectionTimeout() {
    return connectionTimeout;
  }

  public int sessionTimeout() {
    return sessionTimeout;
  }

  @NotNull
  public Serializer serializer() {
    return serializer;
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
  }
}
