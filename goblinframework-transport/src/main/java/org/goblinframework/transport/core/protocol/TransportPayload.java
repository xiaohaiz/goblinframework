package org.goblinframework.transport.core.protocol;

import org.goblinframework.core.util.ArrayUtils;
import org.jetbrains.annotations.Nullable;

@Deprecated
public interface TransportPayload {

  @Nullable
  byte[] readPayload();

  void writePayload(@Nullable byte[] payload);

  @Nullable
  default byte[] drainPayload() {
    byte[] previous = readPayload();
    writePayload(null);
    return previous == null ? ArrayUtils.EMPTY_BYTE_ARRAY : previous;
  }

}
