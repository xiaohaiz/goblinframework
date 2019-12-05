package org.goblinframework.embedded.core.setting;

import org.goblinframework.embedded.core.handler.ServletHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HandlerSetting {

  private final String contextPath;
  private final ServletHandler servletHandler;

  private HandlerSetting(@NotNull HandlerSettingBuilder builder) {
    this.contextPath = Objects.requireNonNull(builder.contextPath);
    this.servletHandler = Objects.requireNonNull(builder.servletHandler);
  }

  @NotNull
  public String contextPath() {
    return contextPath;
  }

  @NotNull
  public ServletHandler servletHandler() {
    return servletHandler;
  }

  @NotNull
  public static HandlerSettingBuilder builder() {
    return new HandlerSettingBuilder();
  }

  final public static class HandlerSettingBuilder {
    private String contextPath = "/";
    private ServletHandler servletHandler;

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
    public HandlerSetting build() {
      return new HandlerSetting(this);
    }
  }
}
