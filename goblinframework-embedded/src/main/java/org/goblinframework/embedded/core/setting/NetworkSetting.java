package org.goblinframework.embedded.core.setting;

import org.goblinframework.core.util.NetworkUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class NetworkSetting {

  private final String host;
  private final int port;

  private NetworkSetting(@NotNull NetworkSettingBuilder builder) {
    this.host = Objects.requireNonNull(builder.host);
    this.port = builder.port;
  }

  @NotNull
  public String host() {
    return host;
  }

  public int port() {
    return port;
  }

  @NotNull
  public static NetworkSettingBuilder builder() {
    return new NetworkSettingBuilder();
  }

  final public static class NetworkSettingBuilder {

    private String host = NetworkUtils.ALL_HOST;
    private int port = NetworkUtils.RANDOM_PORT;

    @NotNull
    public NetworkSettingBuilder host(@NotNull String host) {
      this.host = host;
      return this;
    }

    @NotNull
    public NetworkSettingBuilder port(int port) {
      this.port = port;
      return this;
    }

    @NotNull
    public NetworkSetting build() {
      return new NetworkSetting(this);
    }
  }
}
