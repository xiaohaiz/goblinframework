package org.goblinframework.transport.core.protocol;

import org.goblinframework.serialization.core.Serializer;
import org.goblinframework.serialization.core.manager.SerializerManager;

abstract public class TransportProtocol {

  public static final short MAGIC = (short) 0xbeef;
  public static final byte DEFAULT_SERIALIZER_ID;

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

}
