package org.goblinframework.api.registry;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

final public class RegistryLocation implements Serializable {
  private static final long serialVersionUID = -282395134072809064L;

  @NotNull public final RegistrySystem system;
  @NotNull public final String name;

  public RegistryLocation(@NotNull RegistrySystem system, @NotNull String name) {
    this.system = system;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegistryLocation that = (RegistryLocation) o;
    return system == that.system && name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(system, name);
  }

  @Override
  public String toString() {
    return system + ":" + name;
  }

  @NotNull
  public static RegistryLocation parse(@NotNull String s) {
    List<String> segments = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(s, ":");
    while (st.hasMoreTokens()) {
      String t = st.nextToken();
      segments.add(t);
    }
    if (segments.size() != 2) {
      throw new GoblinRegistryException("Malformed registry location: " + s);
    }
    RegistrySystem system = RegistrySystem.valueOf(segments.get(0));
    String name = segments.get(1);
    return new RegistryLocation(system, name);
  }
}
