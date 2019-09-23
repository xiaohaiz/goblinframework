package org.goblinframework.cache.core.module.monitor;

import org.goblinframework.api.monitor.InstructionTranslator;
import org.goblinframework.core.monitor.InstructionImpl;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final public class VMC extends InstructionImpl {

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
      if (keys == null || keys.isEmpty()) {
        return String.format("%s %s", asLongText(), operation);
      } else {
        String ks = StringUtils.join(keys, " ");
        return String.format("%s %s %s", asLongText(), operation, ks);
      }
    };
  }
}
