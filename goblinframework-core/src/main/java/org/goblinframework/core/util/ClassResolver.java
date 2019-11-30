package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.HashMap;
import java.util.Map;

public class ClassResolver {

  private static final Map<String, Class<?>> primitives;
  private static final ConcurrentReferenceHashMap<String, Class<?>> cache;

  static {
    primitives = new HashMap<>();
    primitives.put("boolean", boolean.class);
    primitives.put("byte", byte.class);
    primitives.put("short", short.class);
    primitives.put("int", int.class);
    primitives.put("long", long.class);
    primitives.put("float", float.class);
    primitives.put("double", double.class);
    primitives.put("char", char.class);

    cache = new ConcurrentReferenceHashMap<>(512);
  }

  @NotNull
  public static Class<?> resolve(@NotNull String name) throws ClassNotFoundException {
    Class<?> p = primitives.get(name);
    if (p != null) {
      return p;
    }

    Class<?> clazz = cache.get(name);
    if (clazz != null) {
      return clazz;
    }
    ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    clazz = ClassUtils.getClass(classLoader, name);
    cache.put(name, clazz);
    return clazz;
  }

}
