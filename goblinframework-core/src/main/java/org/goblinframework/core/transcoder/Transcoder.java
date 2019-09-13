package org.goblinframework.core.transcoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import kotlin.text.Charsets;
import org.goblinframework.core.exception.GoblinTranscodingException;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.util.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;

abstract public class Transcoder {

  public static void encode(@NotNull OutputStream outStream,
                            @NotNull Object obj,
                            @Nullable Serializer serializer) {
    try {
      doEncode(outStream, obj, serializer);
      outStream.flush();
    } catch (GoblinTranscodingException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new GoblinTranscodingException(ex);
    }
  }

  private static void doEncode(@NotNull OutputStream outStream,
                               @NotNull Object obj,
                               @Nullable Serializer serializer) throws Exception {
    if (obj instanceof byte[]) {
      byte[] bs = (byte[]) obj;
      outStream.write(bs);
      return;
    }

    outStream.write(TranscoderConstants.MAGIC_BYTES);

    if (obj instanceof String) {
      outStream.write(0);
      byte[] bs = ((String) obj).getBytes(Charsets.UTF_8);
      outStream.write(bs);
      return;
    }

    if (serializer == null) {
      byte header = TranscoderConstants.JSON_FLAG;
      outStream.write(header);
      ObjectMapper mapper = JsonUtils.getDefaultObjectMapper();
      mapper.writeValue(outStream, obj);
    } else {
      byte header = serializer.mode().getId();
      outStream.write(header);
      serializer.serialize(obj, outStream);
    }
  }

}
