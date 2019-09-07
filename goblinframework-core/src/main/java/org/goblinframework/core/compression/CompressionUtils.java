package org.goblinframework.core.compression;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.goblinframework.api.compression.Compressor;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Compression related helper utilities.
 */
final public class CompressionUtils {

  private static final CompressorStreamFactory FACTORY = new CompressorStreamFactory();

  public static void compress(@NotNull Compressor compressor, @NotNull InputStream inStream, @NotNull OutputStream outStream) {
    CompressorOutputStream cos = null;
    try {
      cos = FACTORY.createCompressorOutputStream(compressor.getAlgorithm(), outStream);
      IOUtils.copy(inStream, cos);
    } catch (Exception ex) {
      throw CompressionException.newInstance(ex);
    } finally {
      IOUtils.closeQuietly(cos);
    }
  }

  public static void decompress(@NotNull Compressor compressor,
                                @NotNull InputStream inStream,
                                @NotNull OutputStream outStream) {
    CompressorInputStream cis = null;
    try {
      cis = FACTORY.createCompressorInputStream(compressor.getAlgorithm(), inStream);
      IOUtils.copy(cis, outStream);
    } catch (Exception ex) {
      throw CompressionException.newInstance(ex);
    } finally {
      IOUtils.closeQuietly(cis);
    }
  }

  public static void compress(@NotNull CompressorMode mode,
                              @NotNull InputStream inStream,
                              @NotNull OutputStream outStream) {
    CompressorOutputStream cos = null;
    try {
      cos = FACTORY.createCompressorOutputStream(mode.getAlgorithm(), outStream);
      IOUtils.copy(inStream, cos);
    } catch (Exception ex) {
      throw CompressionException.newInstance(ex);
    } finally {
      IOUtils.closeQuietly(cos);
    }
  }

  public static void decompress(@NotNull CompressorMode mode,
                                @NotNull InputStream inStream,
                                @NotNull OutputStream outStream) {
    CompressorInputStream cis = null;
    try {
      cis = FACTORY.createCompressorInputStream(mode.getAlgorithm(), inStream);
      IOUtils.copy(cis, outStream);
    } catch (Exception ex) {
      throw CompressionException.newInstance(ex);
    } finally {
      IOUtils.closeQuietly(cis);
    }
  }
}
