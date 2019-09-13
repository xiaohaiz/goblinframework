package org.goblinframework.core.transcoder;

import kotlin.text.Charsets;
import org.goblinframework.core.exception.GoblinTranscodingException;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

abstract public class TranscoderImpl implements Transcoder {

  @Override
  final public void encode(@NotNull Object obj, @NotNull OutputStream outStream) {
    try {
      doEncode(obj, outStream);
      outStream.flush();
    } catch (GoblinTranscodingException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new GoblinTranscodingException(ex);
    }
  }

  private void doEncode(@NotNull Object obj,
                        @NotNull OutputStream outStream) throws Exception {
    if (obj instanceof byte[]) {
      byte[] bs = (byte[]) obj;
      outStream.write(bs);
      return;
    }

    outStream.write(TranscoderUtils.shortToBytes(TranscoderConstants.MAGIC));

    if (obj instanceof String) {
      outStream.write(0);
      byte[] bs = ((String) obj).getBytes(Charsets.UTF_8);
      outStream.write(bs);
      return;
    }

  }
}
