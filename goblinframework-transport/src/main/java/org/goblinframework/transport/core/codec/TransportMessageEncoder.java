package org.goblinframework.transport.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.goblinframework.core.util.JsonUtils;
import org.goblinframework.transport.core.protocol.HandshakeRequest;
import org.goblinframework.transport.core.protocol.HandshakeResponse;
import org.goblinframework.transport.core.protocol.TransportProtocol;

import java.io.OutputStream;
import java.util.LinkedHashMap;

@ChannelHandler.Sharable
public class TransportMessageEncoder extends MessageToByteEncoder<Object> {

  private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

  private static final TransportMessageEncoder instance = new TransportMessageEncoder();

  public static TransportMessageEncoder getInstance() {
    return instance;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    int startIdx = out.writerIndex();
    ByteBufOutputStream bos = new ByteBufOutputStream(out);
    bos.write(LENGTH_PLACEHOLDER);
    bos.writeShort(TransportProtocol.MAGIC);
    if (msg instanceof HandshakeRequest) {
      LinkedHashMap<String, Object> map = ((HandshakeRequest) msg).asMap();
      JsonUtils.getDefaultObjectMapper().writeValue((OutputStream) bos, map);
    } else if (msg instanceof HandshakeResponse) {
      LinkedHashMap<String, Object> map = ((HandshakeResponse) msg).asMap();
      JsonUtils.getDefaultObjectMapper().writeValue((OutputStream) bos, map);
    } else {
      throw new UnsupportedOperationException("Unrecognized message to be encoded: " + msg);
    }
    bos.flush();
    bos.close();
    int endIdx = out.writerIndex();
    out.setInt(startIdx, endIdx - startIdx - 4);
  }
}
