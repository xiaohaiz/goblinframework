package org.goblinframework.webmvc.setting;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

final public class RequestHandlerSetting {

  private final ControllerSetting controllerSetting;
  private final InterceptorSetting interceptorSetting;

  private RequestHandlerSetting(@NotNull RequestHandlerSettingBuilder builder) {
    this.controllerSetting = builder.controllerSettingBuilder.build();
    this.interceptorSetting = builder.interceptorSettingBuilder.build();
  }

  @NotNull
  public ControllerSetting controllerSetting() {
    return controllerSetting;
  }

  @NotNull
  public InterceptorSetting interceptorSetting() {
    return interceptorSetting;
  }

  @NotNull
  public static RequestHandlerSettingBuilder builder() {
    return new RequestHandlerSettingBuilder();
  }

  final public static class RequestHandlerSettingBuilder {

    private final ControllerSetting.ControllerSettingBuilder controllerSettingBuilder;
    private final InterceptorSetting.InterceptorSettingBuilder interceptorSettingBuilder;

    private RequestHandlerSettingBuilder() {
      controllerSettingBuilder = ControllerSetting.builder();
      interceptorSettingBuilder = InterceptorSetting.builder();
    }

    @NotNull
    public RequestHandlerSettingBuilder applyControllerSettingBuilder(@NotNull Consumer<ControllerSetting.ControllerSettingBuilder> block) {
      block.accept(controllerSettingBuilder);
      return this;
    }

    @NotNull
    public RequestHandlerSettingBuilder applyInterceptorSettingBuilder(@NotNull Consumer<InterceptorSetting.InterceptorSettingBuilder> block) {
      block.accept(interceptorSettingBuilder);
      return this;
    }

    @NotNull
    public RequestHandlerSetting build() {
      return new RequestHandlerSetting(this);
    }
  }
}
