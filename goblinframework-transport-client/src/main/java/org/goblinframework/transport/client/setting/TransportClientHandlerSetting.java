package org.goblinframework.transport.client.setting;

import org.goblinframework.transport.client.handler.TransportResponseHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class TransportClientHandlerSetting {

  private final TransportResponseHandler transportResponseHandler;

  private TransportClientHandlerSetting(@NotNull TransportClientHandlerSettingBuilder builder) {
    this.transportResponseHandler = Objects.requireNonNull(builder.transportResponseHandler);
  }

  @NotNull
  public TransportResponseHandler transportResponseHandler() {
    return transportResponseHandler;
  }

  @NotNull
  public static TransportClientHandlerSettingBuilder builder() {
    return new TransportClientHandlerSettingBuilder();
  }

  final public static class TransportClientHandlerSettingBuilder {

    private TransportResponseHandler transportResponseHandler;

    private TransportClientHandlerSettingBuilder() {
    }

    @NotNull
    public TransportClientHandlerSettingBuilder transportResponseHandler(@NotNull TransportResponseHandler transportResponseHandler) {
      this.transportResponseHandler = transportResponseHandler;
      return this;
    }

    @NotNull
    public TransportClientHandlerSetting build() {
      return new TransportClientHandlerSetting(this);
    }
  }
}
