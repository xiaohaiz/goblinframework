package org.goblinframework.transport.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.goblinframework.core.util.JsonUtils;
import org.goblinframework.core.util.TranscoderUtils;
import org.goblinframework.serialization.core.Serializer;
import org.goblinframework.serialization.core.manager.SerializerManager;
import org.goblinframework.transport.core.protocol.*;

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
      bos.writeByte(0);
      LinkedHashMap<String, Object> map = ((HandshakeRequest) msg).asMap();
      JsonUtils.getDefaultObjectMapper().writeValue((OutputStream) bos, map);
    } else if (msg instanceof HandshakeResponse) {
      bos.writeByte(0);
      LinkedHashMap<String, Object> map = ((HandshakeResponse) msg).asMap();
      JsonUtils.getDefaultObjectMapper().writeValue((OutputStream) bos, map);
    } else if (msg instanceof HeartbeatRequest) {
      bos.writeByte(0);
      LinkedHashMap<String, Object> map = ((HeartbeatRequest) msg).asMap();
      JsonUtils.getDefaultObjectMapper().writeValue((OutputStream) bos, map);
    } else if (msg instanceof HeartbeatResponse) {
      bos.writeByte(0);
      LinkedHashMap<String, Object> map = ((HeartbeatResponse) msg).asMap();
      JsonUtils.getDefaultObjectMapper().writeValue((OutputStream) bos, map);
    } else if (msg instanceof ShutdownRequest) {
      bos.writeByte(0);
      LinkedHashMap<String, Object> map = ((ShutdownRequest) msg).asMap();
      JsonUtils.getDefaultObjectMapper().writeValue((OutputStream) bos, map);
    } else if (msg instanceof TransportRequest) {
      byte serializerId = TransportProtocol.DEFAULT_SERIALIZER_ID;
      Serializer serializer = SerializerManager.INSTANCE.getSerializer(serializerId);
      assert serializer != null;
      byte header = (byte) (TransportProtocol.PAYLOAD_FLAG | serializerId);
      bos.writeByte(header);
      byte[] payload = ((TransportRequest) msg).drainPayload();
      assert payload != null;
      byte[] lb = TranscoderUtils.encodeIntPackZeros(payload.length);
      bos.write(lb.length);
      bos.write(lb);
      bos.write(payload);
      serializer.serialize(msg, bos);
    } else if (msg instanceof TransportResponse) {
      byte serializerId = TransportProtocol.DEFAULT_SERIALIZER_ID;
      Serializer serializer = SerializerManager.INSTANCE.getSerializer(serializerId);
      assert serializer != null;
      byte header = (byte) (TransportProtocol.PAYLOAD_FLAG | serializerId);
      bos.writeByte(header);
      byte[] payload = ((TransportResponse) msg).drainPayload();
      assert payload != null;
      byte[] lb = TranscoderUtils.encodeIntPackZeros(payload.length);
      bos.write(lb.length);
      bos.write(lb);
      bos.write(payload);
      serializer.serialize(msg, bos);
    } else {
      throw new UnsupportedOperationException("Unrecognized message to be encoded: " + msg);
    }
    bos.flush();
    bos.close();
    int endIdx = out.writerIndex();
    out.setInt(startIdx, endIdx - startIdx - 4);
  }
}
