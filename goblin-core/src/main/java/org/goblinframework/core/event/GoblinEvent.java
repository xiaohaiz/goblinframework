package org.goblinframework.core.event;

import java.time.Instant;
import java.util.EventObject;

abstract public class GoblinEvent extends EventObject {
  private static final long serialVersionUID = -7526906465983002702L;

  private boolean fair = false;
  private boolean raiseException = true;

  public GoblinEvent() {
    super(Instant.now());
  }

  public boolean isFair() {
    return fair;
  }

  public void setFair(boolean fair) {
    this.fair = fair;
  }

  public boolean isRaiseException() {
    return raiseException;
  }

  public void setRaiseException(boolean raiseException) {
    this.raiseException = raiseException;
  }
}
