package org.goblinframework.webmvc.setting;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

final public class WebmvcSetting {

  private final ControllerSetting controllerSetting;

  private WebmvcSetting(@NotNull WebmvcSettingBuilder builder) {
    this.controllerSetting = builder.controllerSettingBuilder.build();
  }

  @NotNull
  public ControllerSetting controllerSetting() {
    return controllerSetting;
  }

  @NotNull
  public static WebmvcSettingBuilder builder() {
    return new WebmvcSettingBuilder();
  }

  final public static class WebmvcSettingBuilder {

    private final ControllerSetting.ControllerSettingBuilder controllerSettingBuilder;

    private WebmvcSettingBuilder() {
      controllerSettingBuilder = ControllerSetting.builder();
    }

    @NotNull
    public WebmvcSettingBuilder applyControllerSettingBuilder(@NotNull Consumer<ControllerSetting.ControllerSettingBuilder> block) {
      block.accept(controllerSettingBuilder);
      return this;
    }

    @NotNull
    public WebmvcSetting build() {
      return new WebmvcSetting(this);
    }
  }
}
