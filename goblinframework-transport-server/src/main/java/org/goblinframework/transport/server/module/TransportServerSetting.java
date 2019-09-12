package org.goblinframework.transport.server.module;

import org.goblinframework.api.function.Block1;
import org.goblinframework.core.util.NetworkUtils;
import org.goblinframework.transport.server.handler.DefaultHandshakeRequestHandler;
import org.goblinframework.transport.server.handler.HandshakeRequestHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class TransportServerSetting {

  private final String name;
  private final String host;
  private final int port;
  private final HandshakeRequestHandler handshakeRequestHandler;
  private final boolean debugMode;
  private final TransportServerThreadPoolSetting threadPoolSetting;
  private final TransportServerHandlerSetting handlerSetting;

  private TransportServerSetting(@NotNull TransportServerSettingBuilder builder) {
    this.name = Objects.requireNonNull(builder.name);
    this.host = Objects.requireNonNull(builder.host);
    this.port = builder.port;
    this.handshakeRequestHandler = Objects.requireNonNull(builder.handshakeRequestHandler);
    this.debugMode = builder.debugMode;
    this.threadPoolSetting = builder.threadPoolSettingBuilder.build();
    this.handlerSetting = builder.handlerSettingBuilder.build();
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
  public TransportServerHandlerSetting handlerSetting() {
    return handlerSetting;
  }

  @NotNull
  public static TransportServerSettingBuilder builder() {
    return new TransportServerSettingBuilder();
  }

  final public static class TransportServerSettingBuilder {

    private String name;
    private String host = NetworkUtils.ALL_HOST;
    private int port = NetworkUtils.RANDOM_PORT;
    private HandshakeRequestHandler handshakeRequestHandler = DefaultHandshakeRequestHandler.INSTANCE;
    private boolean debugMode = false;
    private final TransportServerThreadPoolSetting.TransportServerThreadPoolSettingBuilder threadPoolSettingBuilder = TransportServerThreadPoolSetting.builder();
    private final TransportServerHandlerSetting.TransportServerHandlerSettingBuilder handlerSettingBuilder = TransportServerHandlerSetting.builder();

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
    public TransportServerSettingBuilder applyThreadPoolSetting(@NotNull Block1<TransportServerThreadPoolSetting.TransportServerThreadPoolSettingBuilder> block) {
      block.apply(threadPoolSettingBuilder);
      return this;
    }

    @NotNull
    public TransportServerSettingBuilder applyHandlerSetting(@NotNull Block1<TransportServerHandlerSetting.TransportServerHandlerSettingBuilder> block) {
      block.apply(handlerSettingBuilder);
      return this;
    }

    @NotNull
    public TransportServerSetting build() {
      return new TransportServerSetting(this);
    }
  }
}
