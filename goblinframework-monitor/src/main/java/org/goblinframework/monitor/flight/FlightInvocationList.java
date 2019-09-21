package org.goblinframework.monitor.flight;

import org.goblinframework.core.monitor.Instruction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

final public class FlightInvocationList extends LinkedList<Instruction> {

  private static final int MAX_CAPACITY = 4096;
  private final AtomicInteger instructionCount = new AtomicInteger();

  public int instructionCount() {
    return instructionCount.get();
  }

  @Override
  public synchronized boolean add(@NotNull Instruction instruction) {
    instructionCount.incrementAndGet();
    if (size() > MAX_CAPACITY) {
      return false;
    }
    return super.add(instruction);
  }

  public synchronized List<Instruction> asList() {
    return new ArrayList<>(this);
  }
}
