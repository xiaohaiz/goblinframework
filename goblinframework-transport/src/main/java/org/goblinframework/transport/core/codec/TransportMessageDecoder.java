package org.goblinframework.transport.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.goblinframework.core.conversion.ConversionService;
import org.goblinframework.core.transcoder.DecodedObject;
import org.goblinframework.core.transcoder.Transcoder;

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
  protected TransportMessage decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    ByteBuf frame = (ByteBuf) super.decode(ctx, in);
    if (frame == null) {
      return TransportMessage.unrecognized();
    }
    try (ByteBufInputStream bis = new ByteBufInputStream(frame)) {
      DecodedObject decoded = Transcoder.decode(bis);
      return ConversionService.INSTANCE.convert(decoded, TransportMessage.class);
    }
  }
}
