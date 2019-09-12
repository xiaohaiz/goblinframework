package org.goblinframework.transport.server.setting;

import org.goblinframework.transport.server.handler.DefaultHandshakeRequestHandler;
import org.goblinframework.transport.server.handler.HandshakeRequestHandler;
import org.goblinframework.transport.server.handler.TransportRequestHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class TransportServerHandlerSetting {

  private final HandshakeRequestHandler handshakeRequestHandler;
  private final TransportRequestHandler transportRequestHandler;

  private TransportServerHandlerSetting(@NotNull TransportServerHandlerSettingBuilder builder) {
    this.handshakeRequestHandler = Objects.requireNonNull(builder.handshakeRequestHandler);
    this.transportRequestHandler = Objects.requireNonNull(builder.transportRequestHandler);
  }

  @NotNull
  public HandshakeRequestHandler handshakeRequestHandler() {
    return handshakeRequestHandler;
  }

  @NotNull
  public TransportRequestHandler transportRequestHandler() {
    return transportRequestHandler;
  }

  @NotNull
  public static TransportServerHandlerSettingBuilder builder() {
    return new TransportServerHandlerSettingBuilder();
  }

  public static class TransportServerHandlerSettingBuilder {

    private HandshakeRequestHandler handshakeRequestHandler = DefaultHandshakeRequestHandler.INSTANCE;
    private TransportRequestHandler transportRequestHandler;

    private TransportServerHandlerSettingBuilder() {
    }

    @NotNull
    public TransportServerHandlerSettingBuilder handshakeRequestHandler(@NotNull HandshakeRequestHandler handshakeRequestHandler) {
      this.handshakeRequestHandler = handshakeRequestHandler;
      return this;
    }

    @NotNull
    public void transportRequestHandler(@NotNull TransportRequestHandler transportRequestHandler) {
      this.transportRequestHandler = transportRequestHandler;
    }

    @NotNull
    public TransportServerHandlerSetting build() {
      return new TransportServerHandlerSetting(this);
    }
  }
}
