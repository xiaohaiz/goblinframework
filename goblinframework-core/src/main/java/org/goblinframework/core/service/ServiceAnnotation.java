package org.goblinframework.core.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

abstract public class ServiceAnnotation {

  public static boolean isAnnotationPresent(@NotNull Class<?> clazz,
                                            @NotNull Class<? extends Annotation> annotationClass) {
    Class<?> classForUse = clazz;
    while (classForUse != null) {
      if (classForUse.isAnnotationPresent(annotationClass)) {
        return true;
      }
      classForUse = classForUse.getSuperclass();
    }
    return false;
  }

  @Nullable
  public static <A extends Annotation> A getAnnotation(@NotNull Class<?> clazz,
                                                       @NotNull Class<A> annotationClass) {
    Class<?> classForUse = clazz;
    while (classForUse != null) {
      A annotation = classForUse.getAnnotation(annotationClass);
      if (annotation != null) {
        return annotation;
      }
      classForUse = classForUse.getSuperclass();
    }
    return null;
  }
}
