package org.goblinframework.transport.core.protocol;

import org.goblinframework.api.common.GoblinException;
import org.goblinframework.core.util.ClassUtils;
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

  private static final IdentityHashMap<Class<?>, Byte> serializerIds = new IdentityHashMap<>();

  static {
    Properties props = new Properties();
    ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    ClassPathResource resource = new ClassPathResource("META-INF/goblin/transport.protocol", classLoader);
    try (InputStream inStream = resource.getInputStream()) {
      props.load(inStream);
      for (String name : props.stringPropertyNames()) {
        String value = props.getProperty(name);
        Class<?> clazz = ClassUtils.loadClass(name);
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

}
