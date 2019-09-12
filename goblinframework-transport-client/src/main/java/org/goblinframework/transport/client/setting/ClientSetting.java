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
  private final boolean debugMode;

  private ClientSetting(ClientSettingBuilder builder) {
    this.name = Objects.requireNonNull(builder.name);
    this.serverId = builder.serverId;
    this.serverHost = Objects.requireNonNull(builder.serverHost);
    this.serverPort = builder.serverPort;
    this.workerThreads = builder.workerThreads;
    this.keepAlive = builder.keepAlive;
    this.connectTimeoutInMillis = builder.connectTimeoutInMillis;
    this.autoReconnect = builder.autoReconnect;
    this.debugMode = builder.debugMode;
  }

  @NotNull
  public String name() {
    return name;
  }

  public String serverId() {
    return serverId;
  }

  @NotNull
  public String serverHost() {
    return serverHost;
  }

  public int serverPort() {
    return serverPort;
  }

  public int workerThreads() {
    return workerThreads;
  }

  public boolean keepAlive() {
    return keepAlive;
  }

  public int connectTimeoutInMillis() {
    return connectTimeoutInMillis;
  }

  public boolean autoReconnect() {
    return autoReconnect;
  }

  public boolean debugMode() {
    return debugMode;
  }

  @NotNull
  public static ClientSettingBuilder builder() {
    return new ClientSettingBuilder();
  }

  final public static class ClientSettingBuilder {

    private String name;
    private String serverId;
    private String serverHost;
    private int serverPort;
    private int workerThreads = SystemUtils.estimateThreads();
    private boolean keepAlive = true;
    private int connectTimeoutInMillis = 30000;
    private boolean autoReconnect = true;
    private boolean debugMode = false;

    public ClientSettingBuilder name(String name) {
      this.name = name;
      return this;
    }

    public ClientSettingBuilder serverId(String serverId) {
      this.serverId = serverId;
      return this;
    }

    public ClientSettingBuilder serverHost(String serverHost) {
      this.serverHost = serverHost;
      return this;
    }

    public ClientSettingBuilder serverPort(int serverPort) {
      this.serverPort = serverPort;
      return this;
    }

    public ClientSettingBuilder workerThreads(int workerThreads) {
      this.workerThreads = workerThreads;
      return this;
    }

    public ClientSettingBuilder keepAlive(boolean keepAlive) {
      this.keepAlive = keepAlive;
      return this;
    }

    public ClientSettingBuilder connectTimeoutInMillis(int connectTimeoutInMillis) {
      this.connectTimeoutInMillis = connectTimeoutInMillis;
      return this;
    }

    public ClientSettingBuilder autoReconnect(boolean autoReconnect) {
      this.autoReconnect = autoReconnect;
      return this;
    }

    @NotNull
    public ClientSettingBuilder debugMode(boolean debugMode) {
      this.debugMode = debugMode;
      return this;
    }

    @NotNull
    public ClientSetting build() {
      return new ClientSetting(this);
    }

  }
}
