package org.goblinframework.core.transcoder;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

@Deprecated
public interface Transcoder1 {

  void encode(@NotNull OutputStream outStream, @NotNull Object obj);

  @NotNull
  default DecodeResult decode(@NotNull InputStream inStream) {
    return Transcoder.decode(inStream);
  }
}
