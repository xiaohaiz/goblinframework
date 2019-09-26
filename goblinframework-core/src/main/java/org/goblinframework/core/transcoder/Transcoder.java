package org.goblinframework.core.transcoder;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

public interface Transcoder {

  void encode(@NotNull OutputStream outStream, @NotNull Object obj);

  @NotNull
  default DecodeResult decode(@NotNull InputStream inStream) {
    return TranscoderUtils.decode(inStream);
  }
}
