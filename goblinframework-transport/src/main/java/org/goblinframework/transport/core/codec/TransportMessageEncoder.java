package org.goblinframework.transport.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.goblinframework.transport.core.protocol.TransportProtocol;

@ChannelHandler.Sharable
public class TransportMessageEncoder extends MessageToByteEncoder<Object> {

  private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    int startIdx = out.writerIndex();
    ByteBufOutputStream bos = new ByteBufOutputStream(out);
    bos.write(LENGTH_PLACEHOLDER);
    bos.writeShort(TransportProtocol.MAGIC);
    bos.flush();
    bos.close();
    int endIdx = out.writerIndex();
    out.setInt(startIdx, endIdx - startIdx - 4);
  }
}
