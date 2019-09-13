package org.goblinframework.transport.core.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import kotlin.text.Charsets;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerManager;
import org.goblinframework.core.transcoder.TranscoderUtils;
import org.goblinframework.core.util.IOUtils;
import org.goblinframework.core.util.JsonUtils;
import org.goblinframework.transport.core.protocol.TransportMessage;
import org.goblinframework.transport.core.protocol.TransportPayload;
import org.goblinframework.transport.core.protocol.TransportProtocol;

import java.io.InputStream;

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
      return TransportMessage.unrecognized();
    }
    short magic = frame.readShort();
    if (magic != TransportProtocol.MAGIC) {
      throw new BadMagicException();
    }
    byte header = frame.readByte();
    boolean payloadEnabled = TransportProtocol.isPayloadEnabled(header);
    boolean typePresented = TransportProtocol.isTypePresented(header);
    byte serializerId = TransportProtocol.extractSerializerId(header);

    Serializer serializer = null;
    if (serializerId != 0) {
      serializer = SerializerManager.INSTANCE.getSerializer(serializerId);
      if (serializer == null) {
        throw new SerializerNotFoundException(serializerId);
      }
    }

    byte[] payload = extractPayloadIfNecessary(frame, payloadEnabled);
    Object message;
    if (serializer == null) {
      message = decodeJsonMessage(frame, typePresented);
    } else {
      message = decodeSerializationMessage(frame, serializer);
    }
    if (message instanceof TransportPayload) {
      ((TransportPayload) message).writePayload(payload);
    }
    return new TransportMessage(message, serializerId);
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

  private Object decodeJsonMessage(ByteBuf buf, boolean typePresented) throws Exception {
    String type = null;
    if (typePresented) {
      int len = buf.readByte();
      byte[] dst = new byte[len];
      buf.readBytes(dst);
      int typeLen = TranscoderUtils.decodeInt(dst);
      dst = new byte[typeLen];
      buf.readBytes(dst);
      type = new String(dst, Charsets.UTF_8);
    }
    try (ByteBufInputStream bis = new ByteBufInputStream(buf)) {
      if (type == null) {
        return IOUtils.toString(bis, Charsets.UTF_8);
      } else {
        Class<?> clazz = ClassResolver.INSTANCE.resolve(type);
        ObjectMapper mapper = JsonUtils.getDefaultObjectMapper();
        return mapper.readValue((InputStream) bis, clazz);
      }
    }
  }

  private Object decodeSerializationMessage(ByteBuf buf, Serializer serializer) throws Exception {
    try (ByteBufInputStream bis = new ByteBufInputStream(buf)) {
      return serializer.deserialize(bis);
    }
  }
}
