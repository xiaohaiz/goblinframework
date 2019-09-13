package org.goblinframework.core.util;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

abstract public class TranscoderUtils {

  public static byte[] shortToBytes(short s) {
    byte[] bs = new byte[2];
    bs[0] = (byte) ((s >> 8) & 0xff);
    bs[1] = (byte) (s & 0xff);
    return bs;
  }

  public static void writeIntPackZeros(int i, @NotNull OutputStream outStream) throws IOException {
    byte[] bs = encodeIntPackZeros(i);
    int len = bs.length;
    byte[] first = new byte[1];
    first[0] = (byte) len;
    outStream.write(first);
    outStream.write(bs);
  }

  public static byte[] encodeIntNoPackZeros(int i) {
    return encodeNumberNoPackZeros(i, 4);
  }

  public static byte[] encodeLongNoPackZeros(long l) {
    return encodeNumberNoPackZeros(l, 8);
  }

  public static byte[] encodeIntPackZeros(int i) {
    return encodeNumberPackZeros(i, 4);
  }

  public static byte[] encodeLongPackZeros(long l) {
    return encodeNumberPackZeros(l, 8);
  }

  public static int decodeInt(@NotNull byte[] b) {
    Validate.isTrue(b.length <= 4);
    return (int) decodeLong(b);
  }

  public static long decodeLong(@NotNull byte[] b) {
    Validate.isTrue(b.length <= 8);
    long rv = 0L;
    int len = b.length;
    //noinspection ForLoopReplaceableByForEach
    for (int var5 = 0; var5 < len; ++var5) {
      byte i = b[var5];
      rv = rv << 8 | (long) (i < 0 ? 256 + i : i);
    }
    return rv;
  }

  private static byte[] encodeNumberPackZeros(long l, int maxBytes) {
    byte[] rv = new byte[maxBytes];
    int firstNon0;
    for (firstNon0 = 0; firstNon0 < rv.length; ++firstNon0) {
      int tmp = rv.length - firstNon0 - 1;
      rv[tmp] = (byte) ((int) (l >> 8 * firstNon0 & 255L));
    }
    //noinspection StatementWithEmptyBody
    for (firstNon0 = 0; firstNon0 < rv.length && rv[firstNon0] == 0; ++firstNon0) {
    }
    if (firstNon0 > 0) {
      byte[] var6 = new byte[rv.length - firstNon0];
      System.arraycopy(rv, firstNon0, var6, 0, rv.length - firstNon0);
      rv = var6;
    }
    return rv;
  }

  private static byte[] encodeNumberNoPackZeros(long l, int maxBytes) {
    byte[] rv = new byte[maxBytes];
    for (int i = 0; i < rv.length; i++) {
      int pos = rv.length - i - 1;
      rv[pos] = (byte) ((l >> (8 * i)) & 0xff);
    }
    return rv;
  }
}
