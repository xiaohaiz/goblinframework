package org.goblinframework.core.compression;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.goblinframework.api.core.CompressorMode;
import org.goblinframework.core.exception.GoblinCompressionException;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Compression related helper utilities.
 */
final public class CompressionUtils {

  private static final CompressorStreamFactory FACTORY = new CompressorStreamFactory();

  public static void compress(@NotNull CompressorMode mode,
                              @NotNull InputStream inStream,
                              @NotNull OutputStream outStream) {
    CompressorOutputStream cos = null;
    try {
      cos = FACTORY.createCompressorOutputStream(mode.getAlgorithm(), outStream);
      IOUtils.copy(inStream, cos);
    } catch (Exception ex) {
      throw new GoblinCompressionException(ex);
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
      throw new GoblinCompressionException(ex);
    } finally {
      IOUtils.closeQuietly(cis);
    }
  }
}
