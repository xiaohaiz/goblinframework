package org.goblinframework.cache.core.module.monitor.instruction;

import org.goblinframework.core.monitor.InstructionImpl;

import java.util.List;

final public class VMC extends InstructionImpl {

  public String operation;
  public List<String> keys;

  public VMC() {
    super(Id.VMC, Mode.SYN, true);
  }
}
