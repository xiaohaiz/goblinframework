package org.goblinframework.monitor.module.monitor.event;

import org.goblinframework.core.exception.GoblinDuplicateException;
import org.goblinframework.core.monitor.Instruction;
import org.goblinframework.core.monitor.InstructionTranslator;
import org.goblinframework.core.util.ServiceInstaller;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;

abstract class InstructionTranslatorManager {

  private static final Map<Class<? extends Instruction>, InstructionTranslator> buffer;

  static {
    buffer = new IdentityHashMap<>();
    ServiceInstaller.installedList(InstructionTranslator.class).forEach(e -> {
      @SuppressWarnings("unchecked")
      Class<? extends Instruction> type = (Class<? extends Instruction>) e.type();
      if (buffer.put(type, e) != null) {
        throw new GoblinDuplicateException();
      }
    });
  }

  @NotNull
  static InstructionTranslator translator(@NotNull Instruction instruction) {
    return buffer.getOrDefault(instruction.getClass(), new InstructionTranslator() {
      @NotNull
      @Override
      public Class type() {
        throw new UnsupportedOperationException();
      }

      @NotNull
      @Override
      public String translate(@NotNull Instruction instruction, boolean pretty) {
        return pretty ? instruction.asLongText() : instruction.asShortText();
      }
    });
  }
}
