package org.goblinframework.core.compression;

import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.goblinframework.core.util.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

  @NotNull
  @Override
  public byte[] compress(@NotNull InputStream inStream) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
    compress(inStream, bos);
    byte[] compressed = bos.toByteArray();
    IOUtils.closeStream(bos);
    return compressed;
  }

  @NotNull
  @Override
  public byte[] compress(@NotNull byte[] data) {
    ByteArrayInputStream bis = new ByteArrayInputStream(data);
    ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
    compress(bis, bos);
    byte[] compressed = bos.toByteArray();
    IOUtils.closeStream(bis);
    IOUtils.closeStream(bos);
    return compressed;
  }

  @Override
  public void decompress(@NotNull InputStream inStream, @NotNull OutputStream outStream) {
    CompressionUtils.decompress(mode, inStream, outStream);
  }

  @NotNull
  @Override
  public byte[] decompress(@NotNull InputStream inStream) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
    decompress(inStream, bos);
    byte[] decompressed = bos.toByteArray();
    IOUtils.closeStream(bos);
    return decompressed;
  }

  @NotNull
  @Override
  public byte[] decompress(@NotNull byte[] data) {
    ByteArrayInputStream bis = new ByteArrayInputStream(data);
    ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
    decompress(bis, bos);
    byte[] decompressed = bos.toByteArray();
    IOUtils.closeStream(bis);
    IOUtils.closeStream(bos);
    return decompressed;
  }

  @NotNull
  @Override
  public CompressorMode getMode() {
    return mode();
  }
}
