package org.goblinframework.cache.redis.module.monitor;

import org.goblinframework.core.monitor.AbstractInstruction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final public class RDS extends AbstractInstruction {

  public String operation;
  public List<String> keys;

  public RDS(@NotNull Mode mode) {
    super(Id.RDS, mode, true);
  }
}
