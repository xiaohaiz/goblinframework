package org.goblinframework.embedded.core.setting;

import org.goblinframework.embedded.core.EmbeddedServerMode;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

final public class EmbeddedServerSetting {

  private final String name;
  private final EmbeddedServerMode mode;
  private final NetworkSetting networkSetting;
  private final ThreadPoolSetting threadPoolSetting;

  private EmbeddedServerSetting(@NotNull Builder builder) {
    this.name = Objects.requireNonNull(builder.name);
    this.mode = Objects.requireNonNull(builder.mode);
    this.networkSetting = builder.networkSettingBuilder.build();
    this.threadPoolSetting = builder.threadPoolSettingBuilder.build();
  }

  @NotNull
  public String name() {
    return name;
  }

  @NotNull
  public EmbeddedServerMode mode() {
    return mode;
  }

  @NotNull
  public NetworkSetting networkSetting() {
    return networkSetting;
  }

  @NotNull
  public ThreadPoolSetting threadPoolSetting() {
    return threadPoolSetting;
  }

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  final public static class Builder {

    private String name;
    private EmbeddedServerMode mode;

    private final NetworkSetting.NetworkSettingBuilder networkSettingBuilder;
    private final ThreadPoolSetting.ThreadPoolSettingBuilder threadPoolSettingBuilder;

    private Builder() {
      networkSettingBuilder = NetworkSetting.builder();
      threadPoolSettingBuilder = ThreadPoolSetting.builder();
    }

    @NotNull
    public Builder name(@NotNull String name) {
      this.name = name;
      return this;
    }

    @NotNull
    public Builder mode(@NotNull EmbeddedServerMode mode) {
      this.mode = mode;
      return this;
    }

    @NotNull
    public Builder applyNetworkSetting(@NotNull Consumer<NetworkSetting.NetworkSettingBuilder> block) {
      block.accept(networkSettingBuilder);
      return this;
    }

    @NotNull
    public Builder applyThreadPoolSetting(@NotNull Consumer<ThreadPoolSetting.ThreadPoolSettingBuilder> block) {
      block.accept(threadPoolSettingBuilder);
      return this;
    }

    @NotNull
    public EmbeddedServerSetting build() {
      return new EmbeddedServerSetting(this);
    }
  }
}
