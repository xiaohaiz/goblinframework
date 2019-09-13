package org.goblinframework.transport.core.protocol.reader;

import kotlin.text.Charsets;
import org.apache.commons.collections4.MapUtils;
import org.goblinframework.api.annotation.ThreadSafe;
import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.compression.CompressorManager;
import org.goblinframework.core.compression.CompressorMode;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerManager;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.transport.core.protocol.TransportResponse;
import org.goblinframework.transport.core.protocol.TransportResponseCode;
import org.goblinframework.transport.core.protocol.TransportResponseException;
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
    if (!TransportResponseCode.isSuccess(response.code)) {
      TransportResponseCode code = TransportResponseCode.parse(response.code);
      String c = MapUtils.getString(response.extensions, "EXCEPTION_CAUSE");
      String m = MapUtils.getString(response.extensions, "EXCEPTION_MESSAGE");
      String message = c + ":" + m;
      throw new TransportResponseException(code, message);
    }

    if (!response.hasPayload) {
      return null;
    }
    byte[] payload = response.payload;
    if (response.compressor != 0) {
      CompressorMode mode = CompressorMode.Companion.mode(response.compressor);
      Objects.requireNonNull(mode);
      Compressor compressor = CompressorManager.INSTANCE.getCompressor(mode);
      payload = compressor.decompress(payload);
    }
    if (response.rawPayload) {
      return payload;
    }
    if (response.serializer == 0) {
      if (payload.length == 0) {
        return StringUtils.EMPTY;
      } else {
        return new String(payload, Charsets.UTF_8);
      }
    } else {
      Serializer serializer = SerializerManager.INSTANCE.getSerializer(response.serializer);
      Objects.requireNonNull(serializer);
      return serializer.deserialize(payload);
    }
  }
}
