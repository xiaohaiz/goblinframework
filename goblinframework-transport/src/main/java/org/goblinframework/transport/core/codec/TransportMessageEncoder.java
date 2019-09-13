package org.goblinframework.transport.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import kotlin.text.Charsets;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerManager;
import org.goblinframework.core.transcoder.TranscoderUtils;
import org.goblinframework.core.util.JsonUtils;
import org.goblinframework.transport.core.protocol.TransportMessage;
import org.goblinframework.transport.core.protocol.TransportPayload;
import org.goblinframework.transport.core.protocol.TransportProtocol;

import java.io.OutputStream;

@ChannelHandler.Sharable
public class TransportMessageEncoder extends MessageToByteEncoder<Object> {

  private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

  private static final TransportMessageEncoder instance = new TransportMessageEncoder();

  public static TransportMessageEncoder getInstance() {
    return instance;
  }

  private TransportMessageEncoder() {
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    if (msg instanceof TransportMessage) {
      encodeTransportMessage(ctx, (TransportMessage) msg, out);
    } else {
      byte serializer = TransportProtocol.getSerializerId(msg.getClass());
      encodeTransportMessage(ctx, new TransportMessage(msg, serializer), out);
    }
  }

  private void encodeTransportMessage(ChannelHandlerContext ctx,
                                      TransportMessage msg,
                                      ByteBuf out) throws Exception {
    if (msg.message == null) {
      throw new UnsupportedOperationException("Encoding unrecognized message not allowed");
    }
    Serializer serializer = null;
    if (msg.serializer != 0) {
      serializer = SerializerManager.INSTANCE.getSerializer(msg.serializer);
      if (serializer == null) {
        throw new SerializerNotFoundException(msg.serializer);
      }
    }
    byte header = (serializer == null ? 0 : serializer.mode().getId());
    if (msg.message instanceof TransportPayload) {
      header = (byte) (header | TransportProtocol.PAYLOAD_FLAG);
    }
    if (serializer == null) {
      header = (byte) (header | TransportProtocol.TYPE_FLAG);
    }

    int startIdx = out.writerIndex();
    ByteBufOutputStream bos = new ByteBufOutputStream(out);
    bos.write(LENGTH_PLACEHOLDER);
    bos.writeShort(TransportProtocol.MAGIC);
    bos.writeByte(header);
    if (msg.message instanceof TransportPayload) {
      TransportPayload tp = (TransportPayload) msg.message;
      byte[] payload = tp.drainPayload();
      assert payload != null;
      TranscoderUtils.writeIntPackZeros(payload.length, bos);
      bos.write(payload);
    }
    if (serializer == null) {
      byte[] type = msg.message.getClass().getName().getBytes(Charsets.UTF_8);
      TranscoderUtils.writeIntPackZeros(type.length, bos);
      bos.write(type);
      JsonUtils.getDefaultObjectMapper().writeValue((OutputStream) bos, msg.message);
    } else {
      serializer.serialize(msg.message, bos);
    }
    bos.flush();
    bos.close();
    int endIdx = out.writerIndex();
    out.setInt(startIdx, endIdx - startIdx - LENGTH_PLACEHOLDER.length);
  }
}
