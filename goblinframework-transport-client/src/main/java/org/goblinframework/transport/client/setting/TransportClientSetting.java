package org.goblinframework.transport.client.setting;

import org.goblinframework.api.core.Block1;
import org.goblinframework.core.util.SystemUtils;
import org.goblinframework.transport.client.handler.DefaultShutdownRequestHandler;
import org.goblinframework.transport.client.handler.ShutdownRequestHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TransportClientSetting {

  private final String name;
  private final String serverId;
  private final String serverHost;
  private final int serverPort;
  private final int workerThreads;
  private final boolean keepAlive;
  private final int connectTimeoutInMillis;
  private final boolean autoReconnect;
  private final boolean receiveShutdown;
  private final boolean sendHeartbeat;
  private final ShutdownRequestHandler shutdownRequestHandler;
  private final boolean debugMode;
  private final TransportClientHandlerSetting handlerSetting;

  private TransportClientSetting(TransportClientSettingBuilder builder) {
    this.name = Objects.requireNonNull(builder.name);
    this.serverId = builder.serverId;
    this.serverHost = Objects.requireNonNull(builder.serverHost);
    this.serverPort = builder.serverPort;
    this.workerThreads = builder.workerThreads;
    this.keepAlive = builder.keepAlive;
    this.connectTimeoutInMillis = builder.connectTimeoutInMillis;
    this.autoReconnect = builder.autoReconnect;
    this.receiveShutdown = builder.receiveShutdown;
    this.sendHeartbeat = builder.sendHeartbeat;
    this.shutdownRequestHandler = builder.shutdownRequestHandler;
    this.debugMode = builder.debugMode;
    this.handlerSetting = builder.handlerSettingBuilder.build();
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

  public boolean receiveShutdown() {
    return receiveShutdown;
  }

  public boolean sendHeartbeat() {
    return sendHeartbeat;
  }

  @NotNull
  public ShutdownRequestHandler shutdownRequestHandler() {
    return shutdownRequestHandler;
  }

  public boolean debugMode() {
    return debugMode;
  }

  @NotNull
  public TransportClientHandlerSetting handlerSetting() {
    return handlerSetting;
  }

  @NotNull
  public static TransportClientSettingBuilder builder() {
    return new TransportClientSettingBuilder();
  }

  final public static class TransportClientSettingBuilder {

    private String name;
    private String serverId;
    private String serverHost;
    private int serverPort;
    private int workerThreads = SystemUtils.estimateThreads();
    private boolean keepAlive = true;
    private int connectTimeoutInMillis = 30000;
    private boolean autoReconnect = true;
    private boolean receiveShutdown = false;
    private boolean sendHeartbeat = false;
    private ShutdownRequestHandler shutdownRequestHandler = DefaultShutdownRequestHandler.INSTANCE;
    private boolean debugMode = false;
    private final TransportClientHandlerSetting.TransportClientHandlerSettingBuilder handlerSettingBuilder = TransportClientHandlerSetting.builder();

    public TransportClientSettingBuilder name(String name) {
      this.name = name;
      return this;
    }

    public TransportClientSettingBuilder serverId(String serverId) {
      this.serverId = serverId;
      return this;
    }

    public TransportClientSettingBuilder serverHost(String serverHost) {
      this.serverHost = serverHost;
      return this;
    }

    public TransportClientSettingBuilder serverPort(int serverPort) {
      this.serverPort = serverPort;
      return this;
    }

    public TransportClientSettingBuilder workerThreads(int workerThreads) {
      this.workerThreads = workerThreads;
      return this;
    }

    public TransportClientSettingBuilder keepAlive(boolean keepAlive) {
      this.keepAlive = keepAlive;
      return this;
    }

    public TransportClientSettingBuilder connectTimeoutInMillis(int connectTimeoutInMillis) {
      this.connectTimeoutInMillis = connectTimeoutInMillis;
      return this;
    }

    @NotNull
    public TransportClientSettingBuilder autoReconnect(boolean autoReconnect) {
      this.autoReconnect = autoReconnect;
      return this;
    }

    @NotNull
    public TransportClientSettingBuilder enableReceiveShutdown() {
      this.receiveShutdown = true;
      return this;
    }

    @NotNull
    public TransportClientSettingBuilder enableSendHeartbeat() {
      this.sendHeartbeat = true;
      return this;
    }

    @NotNull
    public TransportClientSettingBuilder shutdownRequestHandler(ShutdownRequestHandler shutdownRequestHandler) {
      this.shutdownRequestHandler = shutdownRequestHandler;
      return this;
    }

    @NotNull
    public TransportClientSettingBuilder enableDebugMode() {
      this.debugMode = true;
      return this;
    }

    @NotNull
    public TransportClientSettingBuilder applyHandlerSetting(@NotNull Block1<TransportClientHandlerSetting.TransportClientHandlerSettingBuilder> block) {
      block.apply(handlerSettingBuilder);
      return this;
    }

    @NotNull
    public TransportClientSetting build() {
      return new TransportClientSetting(this);
    }

  }
}
