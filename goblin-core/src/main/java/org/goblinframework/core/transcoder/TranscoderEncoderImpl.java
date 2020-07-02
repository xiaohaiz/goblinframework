package org.goblinframework.core.transcoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import kotlin.text.Charsets;
import org.goblinframework.core.compression.Compressor;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.util.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

final class TranscoderEncoderImpl implements TranscoderEncoder {

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
    if (compressor != null && setting.compressionThreshold > 0) {
      byte header = 0;
      // compression is possible
      byte[] bs;
      if (obj instanceof String) {
        bs = ((String) obj).getBytes(Charsets.UTF_8);
      } else if (serializer == null) {
        ObjectMapper mapper = JsonUtils.getDefaultObjectMapper();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(512)) {
          mapper.writeValue(bos, obj);
          bs = bos.toByteArray();
        }
      } else {
        header = (byte) (header | serializer.mode().getId());
        bs = serializer.serialize(obj);
      }
      if (bs.length >= setting.compressionThreshold) {
        byte[] compressed = compressor.compress(bs);
        if (compressed.length < bs.length) {
          header = (byte) (header | ((compressor.mode().getId() << 4) & 0xf0));
          bs = compressed;
        }
      }
      outStream.write(header);
      outStream.write(bs);
    } else {
      // no compression required
      if (obj instanceof String) {
        outStream.write(0);
        byte[] bs = ((String) obj).getBytes(Charsets.UTF_8);
        outStream.write(bs);
      } else if (serializer == null) {
        outStream.write(0);
        ObjectMapper mapper = JsonUtils.getDefaultObjectMapper();
        mapper.writeValue(outStream, obj);
      } else {
        byte header = serializer.mode().getId();
        outStream.write(header);
        serializer.serialize(obj, outStream);
      }
    }
  }
}
