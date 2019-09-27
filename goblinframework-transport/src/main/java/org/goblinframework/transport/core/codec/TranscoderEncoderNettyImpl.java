package org.goblinframework.transport.core.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.*;
import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.transcoder.GoblinTranscoderException;
import org.goblinframework.core.transcoder.TranscoderConstants;
import org.goblinframework.core.transcoder.TranscoderEncoder;
import org.goblinframework.core.transcoder.TranscoderSetting;
import org.goblinframework.core.util.IOUtils;
import org.goblinframework.core.util.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

final class TranscoderEncoderNettyImpl implements TranscoderEncoder {

  @Override
  public void encode(@NotNull TranscoderSetting setting, @NotNull OutputStream outStream, @NotNull Object obj) {
    try {
      executeEncode(setting, outStream, obj);
      outStream.flush();
    } catch (GoblinTranscoderException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new GoblinTranscoderException(ex);
    }
  }

  private void executeEncode(TranscoderSetting setting,
                             OutputStream outStream,
                             Object obj) throws Exception {
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
      buf.writeCharSequence((String) obj, StandardCharsets.UTF_8);
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
      ByteBufInputStream bis = new ByteBufInputStream(buf, true);
      try {
        IOUtils.copy(bis, outStream);
      } finally {
        IOUtils.closeStream(bis);
      }
      return;
    }

    // validate if compression is necessary or not
    int endIdx = buf.writerIndex();
    int actualSize = endIdx - 1;
    if (actualSize < setting.compressionThreshold) {
      ByteBufInputStream bis = new ByteBufInputStream(buf, true);
      try {
        IOUtils.copy(bis, outStream);
      } finally {
        IOUtils.closeStream(bis);
      }
      return;
    }

    // do compress now
    buf.readerIndex(1);
    ByteBuf data = buf.readBytes(buf.readableBytes());
    ByteBuf compressed = ByteBufAllocator.DEFAULT.buffer();
    try {
      ByteBufInputStream inStream = new ByteBufInputStream(data, true);
      try {
        ByteBufOutputStream bos = new ByteBufOutputStream(compressed);
        compressor.compress(inStream, bos);
        bos.flush();
        bos.close();
      } finally {
        IOUtils.closeStream(inStream);
      }
      int compressedSize = compressed.readableBytes();
      if (compressedSize >= actualSize) {
        // give up compress, size enlarged
        buf.readerIndex(0);
        ByteBufInputStream bis = new ByteBufInputStream(buf, true);
        try {
          IOUtils.copy(bis, outStream);
        } finally {
          IOUtils.closeStream(bis);
        }
      } else {
        buf.setIndex(0, 1);
        buf.writeBytes(ByteBufUtil.getBytes(compressed));
        byte header = buf.getByte(0);
        header = (byte) (header | ((compressor.mode().getId() << 4) & 0xf0));
        buf.setByte(0, header);
        ByteBufInputStream bis = new ByteBufInputStream(buf, true);
        try {
          IOUtils.copy(bis, outStream);
        } finally {
          IOUtils.closeStream(bis);
        }
      }
    } finally {
      compressed.release();
    }
  }

}
