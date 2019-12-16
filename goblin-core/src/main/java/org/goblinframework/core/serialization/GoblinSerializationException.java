package org.goblinframework.core.serialization;

import org.goblinframework.api.core.GoblinException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoblinSerializationException extends GoblinException {
  private static final long serialVersionUID = 6366045869964789428L;

  public GoblinSerializationException() {
  }

  public GoblinSerializationException(String message) {
    super(message);
  }

  public GoblinSerializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public GoblinSerializationException(Throwable cause) {
    super(cause);
  }

  @NotNull
  public static GoblinSerializationException requiredSerializable(@Nullable Object obj) {
    String className = (obj == null ? "null" : obj.getClass().getName());
    return new GoblinSerializationException("Class " + className + " does not implement java.io.Serializable");
  }
}
