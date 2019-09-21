package org.goblinframework.monitor.module.monitor.instruction;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.core.monitor.Flight;
import org.goblinframework.core.monitor.InstructionTranslator;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

@Install
final public class DOTTranslator implements InstructionTranslator<DOT> {

  @NotNull
  @Override
  public Class<DOT> type() {
    return DOT.class;
  }

  @NotNull
  @Override
  public String translate(@NotNull DOT instruction, boolean pretty) {
    Flight flight = instruction.getFlight();
    if (!(flight instanceof org.goblinframework.monitor.flight.Flight)) {
      return StringUtils.EMPTY;
    }
    long millis = instruction.dotTime().toEpochMilli();
    long delta = ((org.goblinframework.monitor.flight.Flight) flight).updateDot(millis);
    if (pretty) {
      return String.format("+%sms %s", delta, instruction.dotName());
    } else {
      return String.format("(%s)+%dms", instruction.dotName(), delta);
    }
  }
}
