package org.goblinframework.core.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class ConfigSection implements Serializable {
  private static final long serialVersionUID = 6458401883269302954L;

  private final LinkedHashMap<String, Section> sections = new LinkedHashMap<>();

  @NotNull
  public Section createSection(@NotNull String sectionName) {
    return sections.computeIfAbsent(sectionName, s -> new Section());
  }

  @Nullable
  public Section getSection(@NotNull String sectionName) {
    return sections.get(sectionName);
  }

  final public static class Section extends LinkedHashMap<String, String> {
    private static final long serialVersionUID = 5942129035074122833L;

    private Section() {
    }
  }
}
