package org.goblinframework.api.compression;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

public interface Compression {

  @NotNull
  Compressor getCompressor();

  void compress(@NotNull InputStream inStream, @NotNull OutputStream outStream);

  void decompress(@NotNull InputStream inStream, @NotNull OutputStream outStream);
}
