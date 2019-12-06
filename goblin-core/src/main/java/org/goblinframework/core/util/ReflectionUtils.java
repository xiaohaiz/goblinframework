package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

abstract public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

  public static boolean hasDefaultMethod(@NotNull Class<?> interfaceClass) {
    if (!interfaceClass.isInterface()) {
      throw new IllegalArgumentException("Interface class is required");
    }
    for (Method method : interfaceClass.getMethods()) {
      if (method.isDefault()) {
        return true;
      }
    }
    return false;
  }

  @Nullable
  public static Object invokeInterfaceDefaultMethod(@NotNull Object target, @NotNull Method method, @Nullable Object[] arguments) throws Throwable {
    if (!method.isDefault()) {
      throw new IllegalArgumentException("Default method is required");
    }
    if (SystemUtils.IS_JAVA_1_8) {
      final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
      constructor.setAccessible(true);
      final Class<?> clazz = method.getDeclaringClass();
      return constructor.newInstance(clazz)
          .in(clazz)
          .unreflectSpecial(method, clazz)
          .bindTo(target)
          .invokeWithArguments(arguments);
    } else {
      return MethodHandles.lookup()
          .findSpecial(
              method.getDeclaringClass(),
              method.getName(),
              MethodType.methodType(method.getReturnType(), new Class[0]),
              method.getDeclaringClass()
          ).bindTo(target)
          .invokeWithArguments(arguments);
    }
  }

  @Nullable
  public static Object invoke(@Nullable Object target,
                              @NotNull Method method,
                              @Nullable Object[] args) throws Throwable {
    if (!method.isAccessible()) {
      method.setAccessible(true);
    }
    try {
      return method.invoke(target, args);
    } catch (InvocationTargetException ex) {
      throw ex.getTargetException();
    }
  }

  public static List<Field> allFieldsIncludingAncestors(@NotNull Class<?> clazz,
                                                        boolean includeStatic,
                                                        boolean eliminateDuplicationNames) {
    List<Field> list = new LinkedList<>();
    List<Class<?>> hierarchy = ClassUtils.getClassInheritanceHierarchy(clazz, false);
    for (Class<?> clz : hierarchy) {
      List<Field> fields = Arrays.asList(clz.getDeclaredFields());
      if (!includeStatic) {
        fields = fields.stream()
            .filter(f -> !Modifier.isStatic(f.getModifiers()))
            .collect(Collectors.toList());
      }
      list.addAll(fields);
    }
    Set<String> fieldNames = new HashSet<>();
    List<Field> result = new LinkedList<>();
    if (eliminateDuplicationNames) {
      list.forEach(field -> {
        String fieldName = field.getName();
        if (!fieldNames.contains(fieldName)) {
          fieldNames.add(fieldName);
          result.add(field);
        }
      });
    } else {
      result.addAll(list);
    }
    return result;
  }

  @NotNull
  public static Method getMethod(@NotNull Class<?> clazz,
                                 @NotNull String methodName,
                                 @Nullable String[] parameterTypes)
      throws ClassNotFoundException, NoSuchMethodException {
    Class<?>[] resolvedParameterTypes;
    if (parameterTypes == null || parameterTypes.length == 0) {
      resolvedParameterTypes = new Class[0];
    } else {
      resolvedParameterTypes = new Class[parameterTypes.length];
      for (int i = 0; i < resolvedParameterTypes.length; i++) {
        String parameterType = parameterTypes[i];
        resolvedParameterTypes[i] = ClassResolver.resolve(parameterType);
      }
    }
    return clazz.getMethod(methodName, resolvedParameterTypes);
  }

  @NotNull
  public static Method getDeclaredMethod(@NotNull Class<?> clazz,
                                         @NotNull String methodName,
                                         @Nullable String[] parameterTypes)
      throws ClassNotFoundException, NoSuchMethodException {
    Class<?>[] resolvedParameterTypes;
    if (parameterTypes == null || parameterTypes.length == 0) {
      resolvedParameterTypes = new Class[0];
    } else {
      resolvedParameterTypes = new Class[parameterTypes.length];
      for (int i = 0; i < resolvedParameterTypes.length; i++) {
        String parameterType = parameterTypes[i];
        resolvedParameterTypes[i] = ClassResolver.resolve(parameterType);
      }
    }
    return clazz.getDeclaredMethod(methodName, resolvedParameterTypes);
  }
}
