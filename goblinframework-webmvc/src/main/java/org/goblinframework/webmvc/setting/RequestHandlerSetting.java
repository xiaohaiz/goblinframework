package org.goblinframework.webmvc.setting;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

final public class RequestHandlerSetting {

  private final ControllerSetting controllerSetting;
  private final InterceptorSetting interceptorSetting;
  private final ViewResolverSetting viewResolverSetting;

  private RequestHandlerSetting(@NotNull RequestHandlerSettingBuilder builder) {
    this.controllerSetting = builder.controllerSettingBuilder.build();
    this.interceptorSetting = builder.interceptorSettingBuilder.build();
    this.viewResolverSetting = builder.viewResolverSettingBuilder.build();
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
  public ViewResolverSetting viewResolverSetting() {
    return viewResolverSetting;
  }

  @NotNull
  public static RequestHandlerSettingBuilder builder() {
    return new RequestHandlerSettingBuilder();
  }

  final public static class RequestHandlerSettingBuilder {

    private final ControllerSetting.ControllerSettingBuilder controllerSettingBuilder;
    private final InterceptorSetting.InterceptorSettingBuilder interceptorSettingBuilder;
    private final ViewResolverSetting.ViewResolverSettingBuilder viewResolverSettingBuilder;

    private RequestHandlerSettingBuilder() {
      controllerSettingBuilder = ControllerSetting.builder();
      interceptorSettingBuilder = InterceptorSetting.builder();
      viewResolverSettingBuilder = ViewResolverSetting.builder();
    }

    @NotNull
    public RequestHandlerSettingBuilder applyControllerSetting(@NotNull Consumer<ControllerSetting.ControllerSettingBuilder> block) {
      block.accept(controllerSettingBuilder);
      return this;
    }

    @NotNull
    public RequestHandlerSettingBuilder applyInterceptorSetting(@NotNull Consumer<InterceptorSetting.InterceptorSettingBuilder> block) {
      block.accept(interceptorSettingBuilder);
      return this;
    }

    @NotNull
    public RequestHandlerSettingBuilder applyViewResolverSetting(@NotNull Consumer<ViewResolverSetting.ViewResolverSettingBuilder> block) {
      block.accept(viewResolverSettingBuilder);
      return this;
    }

    @NotNull
    public RequestHandlerSetting build() {
      return new RequestHandlerSetting(this);
    }
  }
}
