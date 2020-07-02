package org.goblinframework.remote.client.module.monitor;

import org.goblinframework.core.monitor.AbstractInstruction;

final public class RIC extends AbstractInstruction {

  public RIC() {
    super(Id.RIC, Mode.ASY, true);
  }
}
