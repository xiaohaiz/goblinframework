package org.goblinframework.embedded.core.manager;

import org.goblinframework.embedded.core.EmbeddedServerMode;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class EmbeddedServerSetting {

  private final String name;
  private final EmbeddedServerMode mode;

  private EmbeddedServerSetting(@NotNull Builder builder) {
    this.name = Objects.requireNonNull(builder.name);
    this.mode = Objects.requireNonNull(builder.mode);
  }

  public String name() {
    return name;
  }

  public EmbeddedServerMode mode() {
    return mode;
  }

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  final public static class Builder {

    private String name;
    private EmbeddedServerMode mode;

    private Builder() {
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder mode(EmbeddedServerMode mode) {
      this.mode = mode;
      return this;
    }

    @NotNull
    public EmbeddedServerSetting build() {
      return new EmbeddedServerSetting(this);
    }
  }
}
