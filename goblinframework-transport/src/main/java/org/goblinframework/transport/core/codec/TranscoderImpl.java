package org.goblinframework.transport.core.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.*;
import kotlin.text.Charsets;
import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.exception.GoblinTranscodingException;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.transcoder.Transcoder;
import org.goblinframework.core.transcoder.TranscoderConstants;
import org.goblinframework.core.transcoder.TranscoderSetting;
import org.goblinframework.core.util.IOUtils;
import org.goblinframework.core.util.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

final class TranscoderImpl implements Transcoder {

  private final TranscoderSetting setting;

  TranscoderImpl(@NotNull TranscoderSetting setting) {
    this.setting = setting;
  }

  @Override
  public void encode(@NotNull OutputStream outStream,
                     @NotNull Object obj) {
    try {
      internalEncode(outStream, obj);
      outStream.flush();
    } catch (GoblinTranscodingException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new GoblinTranscodingException(ex);
    }
  }

  private void internalEncode(@NotNull OutputStream outStream,
                              @NotNull Object obj) throws Exception {
    if (obj instanceof byte[]) {
      byte[] bs = (byte[]) obj;
      outStream.write(bs);
      return;
    }

    outStream.write(TranscoderConstants.MAGIC_BYTES);

    Compressor compressor = setting.compressor;
    Serializer serializer = setting.serializer;
    if (compressor != null && setting.compressionThreshold > 0) {
      byte header = 0;
      byte[] bs;
      ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
      if (obj instanceof String) {
        buf.writeCharSequence((String) obj, Charsets.UTF_8);
      } else if (serializer == null) {
        ObjectMapper mapper = JsonUtils.getDefaultObjectMapper();
        ByteBufOutputStream bos = new ByteBufOutputStream(buf);
        mapper.writeValue((OutputStream) bos, obj);
        bos.flush();
        bos.close();
      } else {
        header = (byte) (header | serializer.mode().getId());
        ByteBufOutputStream bos = new ByteBufOutputStream(buf);
        serializer.serialize(obj, bos);
        bos.flush();
        bos.close();
      }
      bs = ByteBufUtil.getBytes(buf);
      if (bs.length >= setting.compressionThreshold) {
        try (ByteBufInputStream bis = new ByteBufInputStream(buf, true)) {
          int originalSize = buf.readableBytes();
          byte[] compressed = compressor.compress(bis);
          if (compressed.length < originalSize) {
            header = (byte) (header | ((compressor.mode().getId() << 4) & 0xf0));
            outStream.write(header);
            outStream.write(compressed);
          } else {
            outStream.write(header);
            buf.resetReaderIndex();
            IOUtils.copy(bis, outStream);
          }
        }
      } else {
        try (ByteBufInputStream bis = new ByteBufInputStream(buf, true)) {
          outStream.write(header);
          IOUtils.copy(bis, outStream);
        }
      }
    } else {
      ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
      buf.writeByte(0); // write header place holder
      if (obj instanceof String) {
        buf.writeCharSequence((String) obj, Charsets.UTF_8);
      } else if (serializer == null) {
        ObjectMapper mapper = JsonUtils.getDefaultObjectMapper();
        ByteBufOutputStream bos = new ByteBufOutputStream(buf);
        mapper.writeValue((OutputStream) bos, obj);
        bos.flush();
        bos.close();
      } else {
        byte header = serializer.mode().getId();
        ByteBufOutputStream bos = new ByteBufOutputStream(buf);
        serializer.serialize(obj, bos);
        bos.flush();
        bos.close();
        buf.setByte(0, header);
      }
      try (ByteBufInputStream bis = new ByteBufInputStream(buf, true)) {
        IOUtils.copy(bis, outStream);
      }
    }
  }

}
