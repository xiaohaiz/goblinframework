package org.goblinframework.registry.module.monitor;

import org.goblinframework.core.monitor.AbstractInstruction;
import org.jetbrains.annotations.Nullable;

final public class ZKP extends AbstractInstruction {

  @Nullable public String operation;
  @Nullable public String path;
  @Nullable public String createMode;

  public ZKP() {
    super(Id.ZKP, Mode.SYN, true);
  }
}
