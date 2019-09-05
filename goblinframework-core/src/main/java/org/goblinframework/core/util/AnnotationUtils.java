package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

final public class AnnotationUtils {

  /**
   * Interfaces excluded.
   */
  public static boolean isAnnotationPresent(@NotNull Class<?> clazz,
                                            @NotNull Class<? extends Annotation> annotationClass) {
    Class<?> clazzForUse = clazz;
    while (clazzForUse != null) {
      if (clazzForUse.isAnnotationPresent(annotationClass)) {
        return true;
      }
      clazzForUse = clazzForUse.getSuperclass();
    }
    return false;
  }

  /**
   * Interfaces excluded.
   */
  @Nullable
  public static <A extends Annotation> A getAnnotation(@NotNull Class<?> clazz,
                                                       @NotNull Class<A> annotationClass) {
    Class<?> clazzForUse = clazz;
    while (clazzForUse != null) {
      A annotation = clazzForUse.getAnnotation(annotationClass);
      if (annotation != null) {
        return annotation;
      }
      clazzForUse = clazzForUse.getSuperclass();
    }
    return null;
  }

}
