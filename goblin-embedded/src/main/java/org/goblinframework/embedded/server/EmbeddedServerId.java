package org.goblinframework.embedded.server;

import org.goblinframework.api.annotation.HashSafe;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@HashSafe
final public class EmbeddedServerId {

  @NotNull public final EmbeddedServerMode mode;
  @NotNull public final String name;

  public EmbeddedServerId(@NotNull EmbeddedServerMode mode, @NotNull String name) {
    this.mode = mode;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EmbeddedServerId that = (EmbeddedServerId) o;
    return mode == that.mode &&
        name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode, name);
  }
}
