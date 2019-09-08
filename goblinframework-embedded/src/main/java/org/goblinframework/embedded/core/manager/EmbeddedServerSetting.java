package org.goblinframework.embedded.core.manager;

import org.goblinframework.core.util.NetworkUtils;
import org.goblinframework.embedded.core.EmbeddedServerMode;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class EmbeddedServerSetting {

  private final String name;
  private final EmbeddedServerMode mode;
  private final String host;
  private final int port;

  private EmbeddedServerSetting(@NotNull Builder builder) {
    this.name = Objects.requireNonNull(builder.name);
    this.mode = Objects.requireNonNull(builder.mode);
    this.host = Objects.requireNonNull(builder.host);
    this.port = builder.port;
  }

  public String name() {
    return name;
  }

  public EmbeddedServerMode mode() {
    return mode;
  }

  public String host() {
    return host;
  }

  public int port() {
    return port;
  }

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  final public static class Builder {

    private String name;
    private EmbeddedServerMode mode;
    private String host = NetworkUtils.ALL_HOST;
    private int port = NetworkUtils.RANDOM_PORT;

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

    public Builder host(String host) {
      this.host = host;
      return this;
    }

    public Builder port(int port) {
      this.port = port;
      return this;
    }

    @NotNull
    public EmbeddedServerSetting build() {
      return new EmbeddedServerSetting(this);
    }
  }
}
