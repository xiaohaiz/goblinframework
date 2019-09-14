package org.goblinframework.transport.core.protocol;

import org.goblinframework.core.exception.GoblinException;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.transport.core.codec.ClassResolver1;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.IdentityHashMap;
import java.util.Properties;

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

  private static final IdentityHashMap<Class<?>, Byte> serializerIds = new IdentityHashMap<>();

  static {
    Properties props = new Properties();
    ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    ClassPathResource resource = new ClassPathResource("META-INF/goblin/transport.protocol", classLoader);
    try (InputStream inStream = resource.getInputStream()) {
      props.load(inStream);
      for (String name : props.stringPropertyNames()) {
        String value = props.getProperty(name);
        Class<?> clazz = ClassResolver1.INSTANCE.resolve(name);
        byte serializerId = Byte.parseByte(value);
        serializerIds.put(clazz, serializerId);
      }
    } catch (Exception ex) {
      throw new GoblinException(ex);
    }
  }

  public static byte getSerializerId(@NotNull Class<?> clazz) {
    return serializerIds.getOrDefault(clazz, (byte) 0);
  }

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
