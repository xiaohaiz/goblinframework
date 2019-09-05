package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

final public class AnnotationUtils {

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
