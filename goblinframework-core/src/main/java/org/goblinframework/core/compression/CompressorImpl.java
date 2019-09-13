package org.goblinframework.core.compression;

import org.goblinframework.core.exception.GoblinCompressionException;
import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.jetbrains.annotations.NotNull;

import java.io.*;

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
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream(512)) {
      compress(inStream, bos);
      return bos.toByteArray();
    } catch (IOException ex) {
      throw new GoblinCompressionException(ex);
    }
  }

  @NotNull
  @Override
  public byte[] compress(@NotNull byte[] data) {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
         ByteArrayOutputStream bos = new ByteArrayOutputStream(512)) {
      compress(bis, bos);
      return bos.toByteArray();
    } catch (IOException ex) {
      throw new GoblinCompressionException(ex);
    }
  }

  @Override
  public void decompress(@NotNull InputStream inStream, @NotNull OutputStream outStream) {
    CompressionUtils.decompress(mode, inStream, outStream);
  }

  @NotNull
  @Override
  public byte[] decompress(@NotNull InputStream inStream) {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream(512)) {
      decompress(inStream, bos);
      return bos.toByteArray();
    } catch (IOException ex) {
      throw new GoblinCompressionException(ex);
    }
  }

  @NotNull
  @Override
  public byte[] decompress(@NotNull byte[] data) {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
         ByteArrayOutputStream bos = new ByteArrayOutputStream(512)) {
      decompress(bis, bos);
      return bos.toByteArray();
    } catch (IOException ex) {
      throw new GoblinCompressionException(ex);
    }
  }

  @NotNull
  @Override
  public CompressorMode getMode() {
    return mode();
  }

  void close() {
    unregisterIfNecessary();
  }
}
