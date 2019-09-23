package org.goblinframework.api.cache;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class CacheLocation implements Serializable {
  private static final long serialVersionUID = -4538796952895330820L;

  @NotNull public final CacheSystem system;
  @NotNull public final String name;

  public CacheLocation(@NotNull CacheSystem system, @NotNull String name) {
    this.system = system;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CacheLocation that = (CacheLocation) o;
    return system == that.system && name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(system, name);
  }
}
