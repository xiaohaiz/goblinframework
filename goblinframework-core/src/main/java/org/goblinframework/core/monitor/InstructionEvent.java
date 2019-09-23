package org.goblinframework.core.monitor;

import org.goblinframework.api.monitor.Instruction;
import org.goblinframework.core.event.GoblinEvent;
import org.goblinframework.core.event.GoblinEventChannel;
import org.jetbrains.annotations.NotNull;

@GoblinEventChannel("/goblin/monitor")
public class InstructionEvent extends GoblinEvent {

  private final Instruction instruction;

  public InstructionEvent(@NotNull Instruction instruction) {
    this.instruction = instruction;
  }

  @NotNull
  public Instruction getInstruction() {
    return instruction;
  }
}
