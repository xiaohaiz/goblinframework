package org.goblinframework.api.event;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class GoblinEventException extends RuntimeException {
  private static final long serialVersionUID = 2463553713871721311L;

  private final List<Throwable> exceptionList = new LinkedList<>();

  public GoblinEventException(String message) {
    super(message);
  }

  public GoblinEventException(@NotNull Throwable cause) {
    super(cause.getMessage());
    exceptionList.add(cause);
  }

  public GoblinEventException(int totalTask, @NotNull List<Throwable> causeList) {
    super("There are " + causeList.size() + " of " + totalTask + " task(s) failed");
    exceptionList.addAll(causeList);
  }

  public List<Throwable> getExceptionList() {
    return exceptionList;
  }

  @Override
  public synchronized Throwable getCause() {
    return exceptionList.stream().findFirst().orElse(null);
  }

}
