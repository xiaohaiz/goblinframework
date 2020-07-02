package org.goblinframework.cache.redis.module.monitor;

import org.goblinframework.core.monitor.AbstractInstruction;
import org.goblinframework.core.monitor.InstructionTranslator;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final public class RDS extends AbstractInstruction {

  public String operation;
  public List<String> keys;

  public RDS(@NotNull Mode mode) {
    super(Id.RDS, mode, true);
  }

  @NotNull
  @Override
  public InstructionTranslator translator() {
    return pretty -> {
      if (!pretty) {
        return asShortText();
      }
      String command = StringUtils.defaultString(operation);
      if (keys == null || keys.isEmpty()) {
        return String.format("%s %s", asLongText(), command);
      } else {
        return String.format("%s %s %s",
            asLongText(), command, StringUtils.join(keys, " "));
      }
    };
  }
}
