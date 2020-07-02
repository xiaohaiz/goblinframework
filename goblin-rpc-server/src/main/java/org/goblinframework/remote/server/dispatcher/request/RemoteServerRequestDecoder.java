package org.goblinframework.remote.server.dispatcher.request;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.util.ReferenceCountUtil;
import org.goblinframework.core.mapper.JsonMapper;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerManager;
import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedLogger;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.transcoder.DecodeResult;
import org.goblinframework.core.transcoder.GoblinTranscoderException;
import org.goblinframework.core.transcoder.Transcoder;
import org.goblinframework.core.util.IOUtils;
import org.goblinframework.rpc.protocol.RpcRequest;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

@GoblinManagedBean(type = "RemoteServer")
@GoblinManagedLogger(name = "goblin.remote.server.request")
public class RemoteServerRequestDecoder extends GoblinManagedObject
    implements RemoteServerRequestDecoderMXBean {

  RemoteServerRequestDecoder() {
  }

  final static class RequestDecodeResult {
    RpcRequest request;
    Serializer serializer;
    Throwable error;
  }

  @NotNull
  RequestDecodeResult decode(@NotNull byte[] payload) {
    RequestDecodeResult result = new RequestDecodeResult();

    DecodeResult decodeResult;
    ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
    try {
      buf.writeBytes(payload);
      ByteBufInputStream bis = new ByteBufInputStream(buf);
      decodeResult = Transcoder.decode(bis);
      IOUtils.closeStream(bis);
    } finally {
      ReferenceCountUtil.release(buf);
    }
    if (decodeResult.result == null) {
      result.error = new GoblinTranscoderException("Unrecognized remote request");
      return result;
    }

    if (decodeResult.serializer != 0) {
      Serializer serializer = SerializerManager.INSTANCE.getSerializer(decodeResult.serializer);
      if (serializer == null) {
        result.error = new GoblinTranscoderException("Unrecognized remote request serializer: " + decodeResult.serializer);
        return result;
      }
      result.serializer = serializer;
      if (!(decodeResult.result instanceof RpcRequest)) {
        result.error = new GoblinTranscoderException("RemoteRequest required: " + decodeResult.result);
        return result;
      }
      result.request = (RpcRequest) decodeResult.result;
    } else {
      String json = new String((byte[]) decodeResult.result, StandardCharsets.UTF_8);
      RpcRequest request;
      try {
        request = JsonMapper.asObject(json, RpcRequest.class);
      } catch (Exception ex) {
        result.error = ex;
        return result;
      }
      if (request == null) {
        result.error = new GoblinTranscoderException("RemoteRequest required: " + json);
        return result;
      }
      result.request = request;
    }
    return result;
  }
}
