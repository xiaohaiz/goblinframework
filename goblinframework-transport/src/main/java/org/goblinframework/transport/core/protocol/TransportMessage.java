package org.goblinframework.transport.core.protocol;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class TransportMessage implements Serializable {
  private static final long serialVersionUID = 4215017125923074620L;

  public Object message;
  public byte serializer;

  public TransportMessage() {
  }

  public TransportMessage(@Nullable Object message, byte serializer) {
    this.message = message;
    this.serializer = serializer;
  }

  @NotNull
  public static TransportMessage unrecognized() {
    return new TransportMessage();
  }
}
