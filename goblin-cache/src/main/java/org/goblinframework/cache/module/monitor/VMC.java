package org.goblinframework.cache.module.monitor;

import org.goblinframework.core.monitor.AbstractInstruction;
import org.goblinframework.core.monitor.InstructionTranslator;
import org.goblinframework.core.util.CollectionUtils;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final public class VMC extends AbstractInstruction {

  public String operation;
  public List<String> keys;

  public VMC() {
    super(Id.VMC, Mode.SYN, true);
  }

  @NotNull
  @Override
  public InstructionTranslator translator() {
    return pretty -> {
      if (!pretty) {
        return asShortText();
      }
      String operation = StringUtils.defaultString(VMC.this.operation);
      if (CollectionUtils.isEmpty(keys)) {
        return String.format("%s %s", asLongText(), operation);
      } else {
        String ks = StringUtils.join(keys, " ");
        return String.format("%s %s %s", asLongText(), operation, ks);
      }
    };
  }
}
