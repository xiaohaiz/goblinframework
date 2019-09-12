package org.goblinframework.transport.server.module;

import org.goblinframework.api.function.Block1;
import org.goblinframework.core.util.NetworkUtils;
import org.goblinframework.core.util.SystemUtils;
import org.goblinframework.transport.server.handler.DefaultHandshakeRequestHandler;
import org.goblinframework.transport.server.handler.HandshakeRequestHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class TransportServerSetting {

  private final String name;
  private final String host;
  private final int port;
  private final int bossThreads;
  private final int workerThreads;
  private final HandshakeRequestHandler handshakeRequestHandler;
  private final boolean debugMode;
  private final TransportServerThreadPoolSetting threadPoolSetting;

  private TransportServerSetting(@NotNull TransportServerSettingBuilder builder) {
    this.name = Objects.requireNonNull(builder.name);
    this.host = Objects.requireNonNull(builder.host);
    this.port = builder.port;
    this.bossThreads = builder.bossThreads;
    this.workerThreads = builder.workerThreads;
    this.handshakeRequestHandler = Objects.requireNonNull(builder.handshakeRequestHandler);
    this.debugMode = builder.debugMode;
    this.threadPoolSetting = builder.threadPoolSettingBuilder.build();
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

  public boolean debugMode() {
    return debugMode;
  }

  @NotNull
  public TransportServerThreadPoolSetting threadPoolSetting() {
    return threadPoolSetting;
  }

  @NotNull
  public static TransportServerSettingBuilder builder() {
    return new TransportServerSettingBuilder();
  }

  final public static class TransportServerSettingBuilder {

    private String name;
    private String host = NetworkUtils.ALL_HOST;
    private int port = NetworkUtils.RANDOM_PORT;
    private int bossThreads = 1;
    private int workerThreads = SystemUtils.estimateThreads();
    private HandshakeRequestHandler handshakeRequestHandler = DefaultHandshakeRequestHandler.INSTANCE;
    private boolean debugMode = false;
    private final TransportServerThreadPoolSetting.TransportServerThreadPoolSettingBuilder threadPoolSettingBuilder = TransportServerThreadPoolSetting.builder();

    private TransportServerSettingBuilder() {
    }

    @NotNull
    public TransportServerSettingBuilder name(@NotNull String name) {
      this.name = name;
      return this;
    }

    @NotNull
    public TransportServerSettingBuilder host(@NotNull String host) {
      this.host = host;
      return this;
    }

    @NotNull
    public TransportServerSettingBuilder port(int port) {
      this.port = port;
      return this;
    }

    @NotNull
    public TransportServerSettingBuilder bossThreads(int bossThreads) {
      this.bossThreads = SystemUtils.estimateThreads(bossThreads);
      return this;
    }

    @NotNull
    public TransportServerSettingBuilder workerThreads(int workerThreads) {
      this.workerThreads = SystemUtils.estimateThreads(workerThreads);
      return this;
    }

    @NotNull
    public TransportServerSettingBuilder handshakeRequestHandler(@NotNull HandshakeRequestHandler handshakeRequestHandler) {
      this.handshakeRequestHandler = handshakeRequestHandler;
      return this;
    }

    @NotNull
    public TransportServerSettingBuilder enableDebugMode() {
      this.debugMode = true;
      return this;
    }

    @NotNull
    public TransportServerSettingBuilder applyThreadPoolSettings(@NotNull Block1<TransportServerThreadPoolSetting.TransportServerThreadPoolSettingBuilder> block) {
      block.apply(threadPoolSettingBuilder);
      return this;
    }

    @NotNull
    public TransportServerSetting build() {
      return new TransportServerSetting(this);
    }
  }
}
