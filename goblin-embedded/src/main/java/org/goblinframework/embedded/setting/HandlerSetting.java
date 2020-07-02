package org.goblinframework.embedded.setting;

import org.goblinframework.embedded.handler.ServletHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HandlerSetting {

  private final String contextPath;
  private final ServletHandler servletHandler;
  private final boolean enableCompression;

  private HandlerSetting(@NotNull HandlerSettingBuilder builder) {
    this.contextPath = Objects.requireNonNull(builder.contextPath);
    this.servletHandler = Objects.requireNonNull(builder.servletHandler);
    this.enableCompression = builder.enableCompression;
  }

  @NotNull
  public String contextPath() {
    return contextPath;
  }

  @NotNull
  public ServletHandler servletHandler() {
    return servletHandler;
  }

  public boolean enableCompression() {
    return enableCompression;
  }

  @NotNull
  public static HandlerSettingBuilder builder() {
    return new HandlerSettingBuilder();
  }

  final public static class HandlerSettingBuilder {

    private String contextPath = "/";
    private ServletHandler servletHandler;
    private boolean enableCompression = false;

    private HandlerSettingBuilder() {
    }

    @NotNull
    public HandlerSettingBuilder contextPath(@NotNull String contextPath) {
      this.contextPath = contextPath;
      return this;
    }

    @NotNull
    public HandlerSettingBuilder servletHandler(@NotNull ServletHandler servletHandler) {
      this.servletHandler = servletHandler;
      return this;
    }

    @NotNull
    public HandlerSettingBuilder enableCompression(boolean enableCompression) {
      this.enableCompression = enableCompression;
      return this;
    }

    @NotNull
    public HandlerSetting build() {
      return new HandlerSetting(this);
    }
  }
}
