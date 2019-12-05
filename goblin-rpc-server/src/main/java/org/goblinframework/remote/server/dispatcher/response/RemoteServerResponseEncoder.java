package org.goblinframework.remote.server.dispatcher.response;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ReferenceCountUtil;
import org.goblinframework.core.mapper.JsonMapper;
import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedLogger;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.transcoder.Transcoder;
import org.goblinframework.remote.server.invocation.RemoteServerInvocation;
import org.goblinframework.remote.server.module.runtime.RemoteServerTranscoderManager;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

@GoblinManagedBean(type = "RemoteServer")
@GoblinManagedLogger(name = "goblin.remote.server.response")
public class RemoteServerResponseEncoder extends GoblinManagedObject
    implements RemoteServerResponseEncoderMXBean {

  RemoteServerResponseEncoder() {
  }

  final static class ResponseEncodeResult {
    public byte[] content;
    public Throwable error;
  }

  @NotNull
  ResponseEncodeResult encode(@NotNull RemoteServerInvocation invocation) {
    ResponseEncodeResult result = new ResponseEncodeResult();
    try {
      if (invocation.serializer == null) {
        String json = JsonMapper.toJson(invocation.response);
        result.content = json.getBytes(StandardCharsets.UTF_8);
      } else {
        Transcoder transcoder = RemoteServerTranscoderManager.INSTANCE.getTranscoder(invocation.serializer.mode());
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        try {
          try (ByteBufOutputStream bos = new ByteBufOutputStream(buf)) {
            transcoder.encode(bos, invocation.response);
            bos.flush();
          }
          result.content = ByteBufUtil.getBytes(buf);
        } finally {
          ReferenceCountUtil.release(buf);
        }
      }
    } catch (Throwable ex) {
      result.error = ex;
    }
    return result;
  }
}
