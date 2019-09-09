package org.goblinframework.webmvc.setting;

import org.goblinframework.core.util.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

final public class ControllerSetting {

  private final ApplicationContext applicationContext;
  private final List<Object> controllers;

  private ControllerSetting(@NotNull ControllerSettingBuilder builder) {
    this.applicationContext = builder.applicationContext;
    this.controllers = Collections.unmodifiableList(builder.controllers);
  }

  @Nullable
  public ApplicationContext applicationContext() {
    return applicationContext;
  }

  @NotNull
  public List<Object> controllers() {
    return controllers;
  }

  @NotNull
  public static ControllerSettingBuilder builder() {
    return new ControllerSettingBuilder();
  }

  final public static class ControllerSettingBuilder {

    private ApplicationContext applicationContext;
    private final List<Object> controllers = new LinkedList<>();

    private ControllerSettingBuilder() {
    }

    @NotNull
    public ControllerSettingBuilder applicationContext(@Nullable ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
      return this;
    }

    @NotNull
    public ControllerSettingBuilder registerControllers(@Nullable Object... controllers) {
      if (ArrayUtils.isEmpty(controllers)) {
        return this;
      }
      assert controllers != null;
      for (Object controller : controllers) {
        if (controller == null) {
          continue;
        }
        if (this.controllers.stream().noneMatch(e -> e == controller)) {
          this.controllers.add(controller);
        }
      }
      return this;
    }

    @NotNull
    public ControllerSetting build() {
      return new ControllerSetting(this);
    }
  }
}
