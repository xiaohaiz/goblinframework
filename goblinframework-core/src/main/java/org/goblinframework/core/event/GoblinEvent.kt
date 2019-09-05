package org.goblinframework.core.event;

import java.time.Instant;
import java.util.EventObject;

abstract public class GoblinEvent extends EventObject {
  private static final long serialVersionUID = 1272680749868964006L;

  public GoblinEvent() {
    super(Instant.now());
  }

}
