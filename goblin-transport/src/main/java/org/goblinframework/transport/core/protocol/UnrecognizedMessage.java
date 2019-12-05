package org.goblinframework.transport.core.protocol;

import org.goblinframework.api.annotation.Singleton;

import java.io.Serializable;

@Singleton
final public class UnrecognizedMessage implements Serializable {
  private static final long serialVersionUID = -7255801939967809731L;

  public static final UnrecognizedMessage INSTANCE = new UnrecognizedMessage();

  private UnrecognizedMessage() {
  }

  private Object readResolve() {
    return INSTANCE;
  }
}
