package org.goblinframework.transport.core.protocol;

/**
 * <pre>
 * +----------+-----------+----------+
 * | MAGIC(2) | HEADER(1) | DATA ... |
 * +----------+-----------+----------+
 * </pre>
 */
abstract public class TransportProtocol {

  public static final short MAGIC = (short) 0xbeef;           // 1011 1110 1110 1111
  public static final byte PAYLOAD_FLAG = (byte) 0x80;        // 1000 0000
  public static final byte TYPE_FLAG = (byte) 0x40;           // 0100 0000
  private static final byte SERIALIZER_ID_MASK = (byte) 0x3f; // 0011 1111

  public static final byte DEFAULT_SERIALIZER_ID = 1;

  public static boolean isPayloadEnabled(byte header) {
    return (header & PAYLOAD_FLAG) != 0;
  }

  public static boolean isTypePresented(byte header) {
    return (header & TYPE_FLAG) != 0;
  }

  public static byte extractSerializerId(byte header) {
    return (byte) (header & SERIALIZER_ID_MASK);
  }

}
