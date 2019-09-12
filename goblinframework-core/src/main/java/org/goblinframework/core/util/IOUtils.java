package org.goblinframework.core.util;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract public class IOUtils extends org.apache.commons.io.IOUtils {

  public static void closeStream(@Nullable InputStream is) {
    if (is == null) {
      return;
    }
    try {
      is.close();
    } catch (IOException ignore) {
    }
  }

  public static void closeStream(@Nullable OutputStream os) {
    if (os == null) {
      return;
    }
    try {
      os.close();
    } catch (IOException ignore) {
    }
  }
}
