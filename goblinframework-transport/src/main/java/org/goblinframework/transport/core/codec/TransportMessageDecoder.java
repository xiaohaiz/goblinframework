package org.goblinframework.transport.core.codec;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.goblinframework.core.util.JsonUtils;
import org.goblinframework.core.util.TranscoderUtils;
import org.goblinframework.serialization.core.Serializer;
import org.goblinframework.serialization.core.manager.SerializerManager;
import org.goblinframework.transport.core.protocol.*;

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
      return UnrecognizedMessage.INSTANCE;
    }
    if (frame.readableBytes() < TransportProtocol.MAGIC_AND_HEADER_LENGTH) {
      return UnrecognizedMessage.INSTANCE;
    }
    short magic = frame.readShort();
    if (magic != TransportProtocol.MAGIC) {
      return UnrecognizedMessage.INSTANCE;
    }
    byte header = frame.readByte();
    boolean payloadEnabled = TransportProtocol.isPayloadEnabled(header);
    byte serializerId = TransportProtocol.extractSerializerId(header);

    if (serializerId == 0) {
      return decodeJsonMessage(frame, payloadEnabled);
    }

    Serializer serializer = SerializerManager.INSTANCE.getSerializer(serializerId);
    if (serializer == null) {
      return UnrecognizedMessage.INSTANCE;
    }
    byte[] payload = extractPayloadIfNecessary(frame, payloadEnabled);
    ByteBufInputStream bis = new ByteBufInputStream(frame);
    Object ret = serializer.deserialize(bis);
    if (payloadEnabled && ret instanceof TransportPayload) {
      ((TransportPayload) ret).writePayload(payload);
    }
    return ret;
  }

  private byte[] extractPayloadIfNecessary(ByteBuf buf, boolean payloadEnabled) {
    byte[] payload = null;
    if (payloadEnabled) {
      byte[] dst = new byte[buf.readByte()];
      buf.readBytes(dst);
      int payloadLength = TranscoderUtils.decodeInt(dst);
      payload = new byte[payloadLength];
      buf.readBytes(payload);
    }
    return payload;
  }

  private Object decodeJsonMessage(ByteBuf buf, boolean payloadEnabled) throws Exception {
    byte[] payload = extractPayloadIfNecessary(buf, payloadEnabled);
    ByteBufInputStream bis = new ByteBufInputStream(buf);
    JsonNode root = JsonUtils.getDefaultObjectMapper().readTree(bis);
    bis.close();
    if (root == null || !root.isObject()) {
      return UnrecognizedMessage.INSTANCE;
    }
    JsonNode node = root.get("_id");
    if (node == null || !node.isTextual()) {
      return UnrecognizedMessage.INSTANCE;
    }
    String id = node.asText();
    Object ret;
    switch (id) {
      case "HandshakeRequest": {
        ret = JsonUtils.getDefaultObjectMapper().readValue(root.traverse(), HandshakeRequest.class);
        break;
      }
      case "HandshakeResponse": {
        ret = JsonUtils.getDefaultObjectMapper().readValue(root.traverse(), HandshakeResponse.class);
        break;
      }
      case "HeartbeatRequest": {
        ret = JsonUtils.getDefaultObjectMapper().readValue(root.traverse(), HeartbeatRequest.class);
        break;
      }
      case "HeartbeatResponse": {
        ret = JsonUtils.getDefaultObjectMapper().readValue(root.traverse(), HeartbeatResponse.class);
        break;
      }
      default: {
        return UnrecognizedMessage.INSTANCE;
      }
    }
    if (payloadEnabled && ret instanceof TransportPayload) {
      ((TransportPayload) ret).writePayload(payload);
    }
    return ret;
  }
}
