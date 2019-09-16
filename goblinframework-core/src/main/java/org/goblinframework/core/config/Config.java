package org.goblinframework.core.config;

import org.goblinframework.api.annotation.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.LinkedHashMap;

@ThreadSafe(false)
public class Config implements Serializable {
  private static final long serialVersionUID = 6458401883269302954L;

  private final LinkedHashMap<String, Section> sections = new LinkedHashMap<>();

  @NotNull
  public Section section(@NotNull String sectionName) {
    return sections.computeIfAbsent(sectionName, s -> new Section());
  }

  final public static class Section extends LinkedHashMap<String, String> {
    private static final long serialVersionUID = 5942129035074122833L;

    private Section() {
    }
  }
}
