package org.goblinframework.transport.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerManager;
import org.goblinframework.transport.core.protocol.TransportMessage;
import org.goblinframework.transport.core.protocol.TransportPayload;
import org.goblinframework.transport.core.protocol.TransportProtocol;

@ChannelHandler.Sharable
public class TransportMessageEncoder2 extends MessageToByteEncoder<TransportMessage> {

  private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

  private static final TransportMessageEncoder2 instance = new TransportMessageEncoder2();

  public static TransportMessageEncoder2 getInstance() {
    return instance;
  }

  private TransportMessageEncoder2() {
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, TransportMessage msg, ByteBuf out) throws Exception {
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

    // TBC

    bos.flush();
    bos.close();
    int endIdx = out.writerIndex();
    out.setInt(startIdx, endIdx - startIdx - LENGTH_PLACEHOLDER.length);
  }
}
