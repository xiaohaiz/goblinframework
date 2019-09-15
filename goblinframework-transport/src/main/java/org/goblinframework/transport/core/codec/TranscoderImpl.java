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

    ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
    buf.writeByte(0);   // write header placeholder
    if (obj instanceof String) {
      buf.writeCharSequence((String) obj, Charsets.UTF_8);
    } else if (serializer == null) {
      ObjectMapper mapper = JsonUtils.getDefaultObjectMapper();
      ByteBufOutputStream bos = new ByteBufOutputStream(buf);
      mapper.writeValue((OutputStream) bos, obj);
      bos.flush();
      bos.close();
    } else {
      ByteBufOutputStream bos = new ByteBufOutputStream(buf);
      serializer.serialize(obj, bos);
      bos.flush();
      bos.close();
      // reset header
      buf.setByte(0, serializer.mode().getId());
    }

    if (compressor == null || setting.compressionThreshold <= 0) {
      try (ByteBufInputStream bis = new ByteBufInputStream(buf, true)) {
        IOUtils.copy(bis, outStream);
      }
      return;
    }

    // validate if compression is necessary or not
    int endIdx = buf.writerIndex();
    int actualSize = endIdx - 1;
    if (actualSize < setting.compressionThreshold) {
      try (ByteBufInputStream bis = new ByteBufInputStream(buf, true)) {
        IOUtils.copy(bis, outStream);
      }
      return;
    }

    // do compress now
    buf.readerIndex(1);
    ByteBuf data = buf.readBytes(buf.readableBytes());
    ByteBuf compressed = ByteBufAllocator.DEFAULT.buffer();
    try {
      try (ByteBufInputStream bis = new ByteBufInputStream(data, true)) {
        ByteBufOutputStream bos = new ByteBufOutputStream(compressed);
        compressor.compress(bis, bos);
        bos.flush();
        bos.close();
      }
      int compressedSize = compressed.readableBytes();
      if (compressedSize >= actualSize) {
        // give up compress, size enlarged
        buf.readerIndex(0);
        try (ByteBufInputStream bis = new ByteBufInputStream(buf, true)) {
          IOUtils.copy(bis, outStream);
        }
      } else {
        buf.setIndex(1, 1);
        buf.writeBytes(ByteBufUtil.getBytes(compressed));
        byte header = buf.getByte(0);
        header = (byte) (header | ((compressor.mode().getId() << 4) & 0xf0));
        buf.setByte(0, header);
        try (ByteBufInputStream bis = new ByteBufInputStream(buf, true)) {
          IOUtils.copy(bis, outStream);
        }
      }
    } finally {
      compressed.release();
    }
  }

}
