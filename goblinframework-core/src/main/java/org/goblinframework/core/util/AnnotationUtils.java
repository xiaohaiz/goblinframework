package org.goblinframework.core.util;

import org.goblinframework.api.core.ServiceAnnotation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

abstract public class AnnotationUtils extends org.apache.commons.lang3.AnnotationUtils {

  public static boolean isAnnotationPresent(@NotNull Class<?> clazz,
                                            @NotNull Class<? extends Annotation> annotationClass) {
    return ServiceAnnotation.isAnnotationPresent(clazz, annotationClass);
  }

  @Nullable
  public static <A extends Annotation> A getAnnotation(@NotNull Class<?> clazz,
                                                       @NotNull Class<A> annotationClass) {
    return ServiceAnnotation.getAnnotation(clazz, annotationClass);
  }
}
