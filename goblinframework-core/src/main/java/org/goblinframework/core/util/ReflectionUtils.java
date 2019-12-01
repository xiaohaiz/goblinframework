package org.goblinframework.core.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

abstract public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

  public static final MethodHandles.Lookup lookup;

  static {
    try {
      Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
      field.setAccessible(true);
      lookup = (MethodHandles.Lookup) field.get(null);
    } catch (Exception ex) {
      throw new UnsupportedOperationException(ex);
    }
  }

  public static Object invokeInterfaceDefaultMethod(@NotNull Object target, @NotNull Method method, @Nullable Object[] arguments) throws Throwable {
    if (!method.isDefault()) {
      throw new IllegalArgumentException("Default method is required");
    }
    return lookup.in(method.getDeclaringClass())
        .unreflectSpecial(method, method.getDeclaringClass())
        .bindTo(target)
        .invokeWithArguments(arguments);
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

  @SuppressWarnings("unchecked")
  @NotNull
  public static <T> T createProxy(@NotNull final Class<T> interfaceClass,
                                  @NotNull final MethodInterceptor interceptor) {
    if (!interfaceClass.isInterface()) {
      throw new IllegalArgumentException(interfaceClass.getName() + " is not interface");
    }
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setInterfaces(interfaceClass);
    proxyFactory.addAdvice(interceptor);
    return (T) proxyFactory.getProxy(ClassUtils.getDefaultClassLoader());
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
