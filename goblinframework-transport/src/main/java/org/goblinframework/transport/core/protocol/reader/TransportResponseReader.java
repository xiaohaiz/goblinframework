package org.goblinframework.transport.core.protocol.reader;

import org.goblinframework.api.annotation.ThreadSafe;
import org.goblinframework.transport.core.protocol.TransportResponse;
import org.goblinframework.transport.core.protocol.TransportResponseReference;

import java.util.Objects;

@ThreadSafe
public class TransportResponseReader {

  private final TransportResponseReference reference;

  public TransportResponseReader(TransportResponseReference reference) {
    this.reference = Objects.requireNonNull(reference);
  }

  public TransportResponse response() {
    return reference.get();
  }

  public Object readPayload() {
    TransportResponse response = response();
    if (response == null) {
      throw new IllegalStateException("TransportMessageResponse not available");
    }
    return null;
  }
}
