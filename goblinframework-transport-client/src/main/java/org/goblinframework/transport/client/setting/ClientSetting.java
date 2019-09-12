package org.goblinframework.transport.client.setting;

import org.goblinframework.core.util.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ClientSetting {

  private final String name;
  private final String serverId;
  private final String serverHost;
  private final int serverPort;
  private final int workerThreads;
  private final boolean keepAlive;
  private final int connectTimeoutInMillis;
  private final boolean autoReconnect;

  private ClientSetting(Builder builder) {
    this.name = Objects.requireNonNull(builder.name);
    this.serverId = builder.serverId;
    this.serverHost = Objects.requireNonNull(builder.serverHost);
    this.serverPort = builder.serverPort;
    this.workerThreads = builder.workerThreads;
    this.keepAlive = builder.keepAlive;
    this.connectTimeoutInMillis = builder.connectTimeoutInMillis;
    this.autoReconnect = builder.autoReconnect;
  }

  @NotNull
  public String name() {
    return name;
  }

  public String getServerId() {
    return serverId;
  }

  public String getServerHost() {
    return serverHost;
  }

  public int getServerPort() {
    return serverPort;
  }

  public int getWorkerThreads() {
    return workerThreads;
  }

  public boolean isKeepAlive() {
    return keepAlive;
  }

  public int getConnectTimeoutInMillis() {
    return connectTimeoutInMillis;
  }

  public boolean isAutoReconnect() {
    return autoReconnect;
  }

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  final public static class Builder {

    private String name;
    private String serverId;
    private String serverHost;
    private int serverPort;
    private int workerThreads = SystemUtils.estimateThreads();
    private boolean keepAlive = true;
    private int connectTimeoutInMillis = 30000;
    private boolean autoReconnect = true;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder serverId(String serverId) {
      this.serverId = serverId;
      return this;
    }

    public Builder serverHost(String serverHost) {
      this.serverHost = serverHost;
      return this;
    }

    public Builder serverPort(int serverPort) {
      this.serverPort = serverPort;
      return this;
    }

    public Builder workerThreads(int workerThreads) {
      this.workerThreads = workerThreads;
      return this;
    }

    public Builder keepAlive(boolean keepAlive) {
      this.keepAlive = keepAlive;
      return this;
    }

    public Builder connectTimeoutInMillis(int connectTimeoutInMillis) {
      this.connectTimeoutInMillis = connectTimeoutInMillis;
      return this;
    }

    public Builder autoReconnect(boolean autoReconnect) {
      this.autoReconnect = autoReconnect;
      return this;
    }

    @NotNull
    public ClientSetting build() {
      return new ClientSetting(this);
    }

  }
}
