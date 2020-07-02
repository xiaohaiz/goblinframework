package org.goblinframework.core.util;

import org.goblinframework.api.core.GoblinException;
import reactor.core.Exceptions;

abstract public class ExceptionUtils extends org.apache.commons.lang3.exception.ExceptionUtils {

  public static RuntimeException propagate(Throwable t) {
    Exceptions.throwIfJvmFatal(t);
    if (t instanceof RuntimeException) {
      return (RuntimeException) t;
    }
    return new GoblinException(t);
  }
}
