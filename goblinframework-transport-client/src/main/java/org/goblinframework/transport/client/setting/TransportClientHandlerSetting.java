package org.goblinframework.transport.client.setting;

import org.goblinframework.transport.client.flight.MessageFlightResponseHandler;
import org.goblinframework.transport.client.handler.TransportClientConnectedHandler;
import org.goblinframework.transport.client.handler.TransportResponseHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

final public class TransportClientHandlerSetting {

  private final TransportResponseHandler transportResponseHandler;
  private final TransportClientConnectedHandler transportClientConnectedHandler;

  private TransportClientHandlerSetting(@NotNull TransportClientHandlerSettingBuilder builder) {
    this.transportResponseHandler = Objects.requireNonNull(builder.transportResponseHandler);
    this.transportClientConnectedHandler = builder.transportClientConnectedHandler;
  }

  @NotNull
  public TransportResponseHandler transportResponseHandler() {
    return transportResponseHandler;
  }

  @Nullable
  public TransportClientConnectedHandler transportClientConnectedHandler() {
    return transportClientConnectedHandler;
  }

  @NotNull
  public static TransportClientHandlerSettingBuilder builder() {
    return new TransportClientHandlerSettingBuilder();
  }

  final public static class TransportClientHandlerSettingBuilder {

    private TransportResponseHandler transportResponseHandler;
    private TransportClientConnectedHandler transportClientConnectedHandler;

    private TransportClientHandlerSettingBuilder() {
    }

    @NotNull
    public TransportClientHandlerSettingBuilder transportResponseHandler(@NotNull TransportResponseHandler transportResponseHandler) {
      this.transportResponseHandler = transportResponseHandler;
      return this;
    }

    @NotNull
    public TransportClientHandlerSettingBuilder enableMessageFlight() {
      transportResponseHandler(MessageFlightResponseHandler.INSTANCE);
      return this;
    }

    @NotNull
    public TransportClientHandlerSettingBuilder transportClientConnectedHandler(@NotNull TransportClientConnectedHandler transportClientConnectedHandler) {
      this.transportClientConnectedHandler = transportClientConnectedHandler;
      return this;
    }

    @NotNull
    public TransportClientHandlerSetting build() {
      return new TransportClientHandlerSetting(this);
    }
  }
}
