package org.goblinframework.core.compression;

import org.goblinframework.api.core.CompressorMode;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

public interface Compressor {

  @NotNull
  CompressorMode mode();

  void compress(@NotNull InputStream inStream, @NotNull OutputStream outStream);

  @NotNull
  byte[] compress(@NotNull InputStream inStream);

  @NotNull
  byte[] compress(@NotNull byte[] data);

  void decompress(@NotNull InputStream inStream, @NotNull OutputStream outStream);

  @NotNull
  byte[] decompress(@NotNull InputStream inStream);

  @NotNull
  byte[] decompress(@NotNull byte[] data);
}
