package org.goblinframework.transport.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.goblinframework.transport.core.protocol.TransportProtocol;

public class TransportMessageDecoder extends LengthFieldBasedFrameDecoder {

  public static TransportMessageDecoder newInstance() {
    return new TransportMessageDecoder();
  }

  private TransportMessageDecoder() {
    super(Integer.MAX_VALUE, 0, 4, 0, 4);
  }

  @Override
  protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
    return buffer.slice(index, length);
  }

  @Override
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    ByteBuf frame = (ByteBuf) super.decode(ctx, in);
    if (frame == null) {
      return null;
    }
    if (frame.readableBytes() < TransportProtocol.MAGIC_AND_HEADER_LENGTH) {
      return null;
    }
    short magic = frame.readShort();
    if (magic != TransportProtocol.MAGIC) {
      return null;
    }
    byte header = frame.readByte();
    boolean payloadEnabled = TransportProtocol.isPayloadEnabled(header);
    byte serializerId = TransportProtocol.extractSerializerId(header);

    if (serializerId == 0) {
      return decodeJsonMessage(frame, payloadEnabled);
    }

    return null;
  }

  private Object decodeJsonMessage(ByteBuf buf, boolean payloadEnabled) {
    if (payloadEnabled) {
      byte[] dst = new byte[buf.readByte()];
      buf.readBytes(dst);

    }
    return null;
  }
}
