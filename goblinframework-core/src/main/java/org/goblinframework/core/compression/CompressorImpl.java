package org.goblinframework.core.compression;

import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

@GoblinManagedBean(type = "CORE", name = "Compressor")
final class CompressorImpl extends GoblinManagedObject implements Compressor, CompressorMXBean {

  private final CompressorMode mode;

  CompressorImpl(@NotNull CompressorMode mode) {
    this.mode = mode;
  }

  @NotNull
  @Override
  public CompressorMode mode() {
    return mode;
  }

  @Override
  public void compress(@NotNull InputStream inStream, @NotNull OutputStream outStream) {
    CompressionUtils.compress(mode, inStream, outStream);
  }

  @Override
  public void decompress(@NotNull InputStream inStream, @NotNull OutputStream outStream) {
    CompressionUtils.decompress(mode, inStream, outStream);
  }

  @NotNull
  @Override
  public CompressorMode getMode() {
    return mode();
  }
}
