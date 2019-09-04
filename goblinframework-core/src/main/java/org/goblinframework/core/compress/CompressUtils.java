package org.goblinframework.core.compress;

import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Compression related helper utilities.
 */
final public class CompressUtils {

  private static final CompressorStreamFactory FACTORY = new CompressorStreamFactory();

  public static void compress(@NotNull InputStream inStream, @NotNull OutputStream outStream) {
    String name = CompressorStreamFactory.GZIP;
    CompressorOutputStream cos = null;
    try {
      cos = FACTORY.createCompressorOutputStream(name, outStream);
      IOUtils.copy(inStream, outStream);
    } catch (Exception ex) {
      throw CompressException.newInstance(ex);
    } finally {
      IOUtils.closeQuietly(cos);
    }
  }
}
