package org.goblinframework.embedded.setting;

import org.goblinframework.embedded.server.EmbeddedServerMode;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Consumer;

final public class ServerSetting {

  private final String name;
  private final EmbeddedServerMode mode;
  private final NetworkSetting networkSetting;
  private final ThreadPoolSetting threadPoolSetting;
  private final HandlerSettings handlerSettings;

  private ServerSetting(@NotNull ServerSettingBuilder builder) {
    this.name = Objects.requireNonNull(builder.name);
    this.mode = Objects.requireNonNull(builder.mode);
    this.networkSetting = builder.networkSettingBuilder.build();
    this.threadPoolSetting = builder.threadPoolSettingBuilder.build();
    this.handlerSettings = new HandlerSettings();
    for (HandlerSetting.HandlerSettingBuilder handlerSettingBuilder : builder.handlerSettingBuilders) {
      HandlerSetting handlerSetting = handlerSettingBuilder.build();
      String contextPath = handlerSetting.contextPath();
      if (!handlerSettings.addSetting(contextPath, handlerSetting)) {
        throw new IllegalArgumentException("Duplicated Handler.contextPath: " + contextPath);
      }
    }
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
  public HandlerSettings handlerSettings() {
    return handlerSettings;
  }

  @NotNull
  public static ServerSettingBuilder builder() {
    return new ServerSettingBuilder();
  }

  final public static class ServerSettingBuilder {

    private String name;
    private EmbeddedServerMode mode;

    private final NetworkSetting.NetworkSettingBuilder networkSettingBuilder;
    private final ThreadPoolSetting.ThreadPoolSettingBuilder threadPoolSettingBuilder;
    private final LinkedList<HandlerSetting.HandlerSettingBuilder> handlerSettingBuilders;

    private ServerSettingBuilder() {
      networkSettingBuilder = NetworkSetting.builder();
      threadPoolSettingBuilder = ThreadPoolSetting.builder();
      handlerSettingBuilders = new LinkedList<>();
      handlerSettingBuilders.add(HandlerSetting.builder());
    }

    @NotNull
    public ServerSettingBuilder name(@NotNull String name) {
      this.name = name;
      return this;
    }

    @NotNull
    public ServerSettingBuilder mode(@NotNull EmbeddedServerMode mode) {
      this.mode = mode;
      return this;
    }

    @NotNull
    public ServerSettingBuilder applyNetworkSetting(@NotNull Consumer<NetworkSetting.NetworkSettingBuilder> block) {
      block.accept(networkSettingBuilder);
      return this;
    }

    @NotNull
    public ServerSettingBuilder applyThreadPoolSetting(@NotNull Consumer<ThreadPoolSetting.ThreadPoolSettingBuilder> block) {
      block.accept(threadPoolSettingBuilder);
      return this;
    }

    @NotNull
    public ServerSettingBuilder applyHandlerSetting(@NotNull Consumer<HandlerSetting.HandlerSettingBuilder> block) {
      HandlerSetting.HandlerSettingBuilder last = handlerSettingBuilders.getLast();
      block.accept(last);
      return this;
    }

    @NotNull
    public ServerSettingBuilder nextHandlerSetting() {
      handlerSettingBuilders.add(HandlerSetting.builder());
      return this;
    }

    @NotNull
    public ServerSetting build() {
      return new ServerSetting(this);
    }
  }
}
