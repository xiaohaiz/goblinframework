package org.goblinframework.remote.client.dispatcher.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.util.ReferenceCountUtil;
import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.core.mapper.JsonMapper;
import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.transcoder.DecodeResult;
import org.goblinframework.core.transcoder.GoblinTranscoderException;
import org.goblinframework.core.transcoder.Transcoder;
import org.goblinframework.rpc.protocol.RpcResponse;
import org.jetbrains.annotations.NotNull;

@GoblinManagedBean(type = "RemoteClient")
public class RemoteClientResponseDecoder extends GoblinManagedObject
    implements RemoteClientResponseDecoderMXBean {

  RemoteClientResponseDecoder() {
  }

  @NotNull
  ResponseDecodeResult decode(@NotNull byte[] content) {
    ResponseDecodeResult result = new ResponseDecodeResult();
    ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
    try {
      buf.writeBytes(content);
      DecodeResult decoded;
      try (ByteBufInputStream bis = new ByteBufInputStream(buf)) {
        decoded = Transcoder.decode(bis);
      }
      if (decoded.result == null) {
        result.error = new GoblinTranscoderException("Unrecognized response received");
        return result;
      }
      if (decoded.serializer != 0) {
        // Binary encoded
        SerializerMode serializer = SerializerMode.Companion.parse(decoded.serializer);
        if (serializer == null) {
          result.error = new GoblinTranscoderException("Unrecognized serializer: " + decoded.serializer);
          return result;
        }
        result.serializer = serializer;
        if (!(decoded.result instanceof RpcResponse)) {
          result.error = new GoblinTranscoderException("Decode result is not RemoteResponse: " + decoded.result);
          return result;
        }
        result.response = (RpcResponse) decoded.result;
      } else {
        // JSON encoded
        ObjectMapper mapper = JsonMapper.getDefaultObjectMapper();
        RpcResponse response = mapper.readValue((byte[]) decoded.result, RpcResponse.class);
        if (response == null) {
          result.error = new GoblinTranscoderException("Unrecognized RemoteResponse JSON");
          return result;
        }
        result.response = response;
      }
    } catch (Throwable ex) {
      result.error = GoblinTranscoderException.rethrow(ex);
    } finally {
      ReferenceCountUtil.release(buf);
    }
    return result;
  }

  final public static class ResponseDecodeResult {
    public RpcResponse response;
    public SerializerMode serializer;
    public Throwable error;
  }
}
