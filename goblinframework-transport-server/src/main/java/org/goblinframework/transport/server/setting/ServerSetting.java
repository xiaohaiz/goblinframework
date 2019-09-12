package org.goblinframework.transport.server.setting;

import org.goblinframework.core.util.NetworkUtils;
import org.goblinframework.core.util.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class ServerSetting {

  private final String name;
  private final String host;
  private final int port;
  private final int bossThreads;
  private final int workerThreads;
  private final HandshakeRequestHandler handshakeRequestHandler;

  private ServerSetting(@NotNull ServerSettingBuilder builder) {
    this.name = Objects.requireNonNull(builder.name);
    this.host = Objects.requireNonNull(builder.host);
    this.port = builder.port;
    this.bossThreads = builder.bossThreads;
    this.workerThreads = builder.workerThreads;
    this.handshakeRequestHandler = Objects.requireNonNull(builder.handshakeRequestHandler);
  }

  @NotNull
  public String name() {
    return name;
  }

  @NotNull
  public String host() {
    return host;
  }

  public int port() {
    return port;
  }

  public int bossThreads() {
    return bossThreads;
  }

  public int workerThreads() {
    return workerThreads;
  }

  @NotNull
  public HandshakeRequestHandler handshakeRequestHandler() {
    return handshakeRequestHandler;
  }

  @NotNull
  public static ServerSettingBuilder builder() {
    return new ServerSettingBuilder();
  }

  final public static class ServerSettingBuilder {

    private String name;
    private String host = NetworkUtils.ALL_HOST;
    private int port = NetworkUtils.RANDOM_PORT;
    private int bossThreads = 1;
    private int workerThreads = SystemUtils.estimateThreads();
    private HandshakeRequestHandler handshakeRequestHandler = DefaultHandshakeRequestHandler.INSTANCE;

    private ServerSettingBuilder() {
    }

    @NotNull
    public ServerSettingBuilder name(@NotNull String name) {
      this.name = name;
      return this;
    }

    @NotNull
    public ServerSettingBuilder setHost(@NotNull String host) {
      this.host = host;
      return this;
    }

    @NotNull
    public ServerSettingBuilder setPort(int port) {
      this.port = port;
      return this;
    }

    @NotNull
    public ServerSettingBuilder bossThreads(int bossThreads) {
      this.bossThreads = SystemUtils.estimateThreads(bossThreads);
      return this;
    }

    @NotNull
    public ServerSettingBuilder workerThreads(int workerThreads) {
      this.workerThreads = SystemUtils.estimateThreads(workerThreads);
      return this;
    }

    @NotNull
    public ServerSettingBuilder handshakeRequestHandler(@NotNull HandshakeRequestHandler handshakeRequestHandler) {
      this.handshakeRequestHandler = handshakeRequestHandler;
      return this;
    }

    @NotNull
    public ServerSetting build() {
      return new ServerSetting(this);
    }
  }
}
