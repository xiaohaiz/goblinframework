package org.goblinframework.core.transcoder;

/**
 * Transcode related constants' definition.
 */
abstract public class TranscoderConstants {

  public static final short MAGIC = (short) 0xbeef;       // 1011 1110 1110 1111
  public static final byte FLAG_MASK = (byte) 0xf0;       // 1111 0000
  public static final byte SERIALIZER_MASK = (byte) 0xf;  // 0000 1111
  public static final byte[] MAGIC_BYTES;

  static {
    MAGIC_BYTES = TranscoderUtils.shortToBytes(MAGIC);
  }

}
