package org.goblinframework.transport.core.protocol;

import org.goblinframework.serialization.core.Serializer;
import org.goblinframework.serialization.core.manager.SerializerManager;

/**
 * <pre>
 * +----------+-----------+----------+
 * | MAGIC(2) | HEADER(1) | DATA ... |
 * +----------+-----------+----------+
 * </pre>
 */
abstract public class TransportProtocol {

  public static final short MAGIC = (short) 0xbeef;
  public static final byte DEFAULT_SERIALIZER_ID;
  public static final byte PAYLOAD_FLAG = (byte) 0x80;
  public static final byte SERIALIZER_ID_MASK = (byte) 0x7f;
  public static final int MAGIC_AND_HEADER_LENGTH = 3;

  static {
    byte serializerId;
    SerializerManager serializerManager = SerializerManager.INSTANCE;
    if (serializerManager.getSerializer(Serializer.HESSIAN2) != null) {
      serializerId = Serializer.HESSIAN2;
    } else {
      serializerId = Serializer.JAVA;
    }
    DEFAULT_SERIALIZER_ID = serializerId;
  }

  public static boolean isPayloadEnabled(byte header) {
    return (header & PAYLOAD_FLAG) != 0;
  }

  public static byte extractSerializerId(byte header) {
    return (byte) (header & SERIALIZER_ID_MASK);
  }

}
