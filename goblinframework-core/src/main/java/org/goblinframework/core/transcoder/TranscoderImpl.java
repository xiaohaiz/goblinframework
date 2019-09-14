package org.goblinframework.core.transcoder;

import org.goblinframework.core.exception.GoblinTranscodingException;
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
  }
}
