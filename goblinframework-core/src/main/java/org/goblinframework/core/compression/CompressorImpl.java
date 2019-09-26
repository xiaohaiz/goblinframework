package org.goblinframework.core.compression;

import org.goblinframework.api.core.GoblinManagedBean;
import org.goblinframework.api.core.GoblinManagedObject;
import org.goblinframework.core.exception.GoblinCompressionException;
import org.goblinframework.core.util.StopWatch;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.concurrent.atomic.LongAdder;

@GoblinManagedBean(type = "CORE", name = "Compressor")
final class CompressorImpl extends GoblinManagedObject implements Compressor, CompressorMXBean {

  private final CompressorMode mode;
  private final StopWatch watch = new StopWatch();
  private final LongAdder compressCount = new LongAdder();
  private final LongAdder compressExceptionCount = new LongAdder();
  private final LongAdder decompressCount = new LongAdder();
  private final LongAdder decompressExceptionCount = new LongAdder();

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
    try {
      CompressionUtils.compress(mode, inStream, outStream);
    } catch (GoblinCompressionException ex) {
      compressExceptionCount.increment();
      throw ex;
    } finally {
      compressCount.increment();
    }
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
    try {
      CompressionUtils.decompress(mode, inStream, outStream);
    } catch (GoblinCompressionException ex) {
      decompressExceptionCount.increment();
      throw ex;
    } finally {
      decompressCount.increment();
    }
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
  public String getUpTime() {
    return watch.toString();
  }

  @NotNull
  @Override
  public CompressorMode getMode() {
    return mode();
  }

  @Override
  public long getCompressCount() {
    return compressCount.sum();
  }

  @Override
  public long getCompressExceptionCount() {
    return compressExceptionCount.sum();
  }

  @Override
  public long getDecompressCount() {
    return decompressCount.sum();
  }

  @Override
  public long getDecompressExceptionCount() {
    return decompressExceptionCount.sum();
  }

  @Override
  protected void disposeBean() {
    watch.stop();
  }
}
