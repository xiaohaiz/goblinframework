package org.goblinframework.webmvc.setting;

import org.goblinframework.core.util.ArrayUtils;
import org.goblinframework.webmvc.view.ViewResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

final public class ViewResolverSetting {

  private ApplicationContext applicationContext;
  private final List<ViewResolver> viewResolvers;

  private ViewResolverSetting(@NotNull ViewResolverSettingBuilder builder) {
    this.applicationContext = builder.applicationContext;
    this.viewResolvers = Collections.unmodifiableList(builder.viewResolvers);
  }

  @Nullable
  public ApplicationContext applicationContext() {
    return applicationContext;
  }

  @NotNull
  public List<ViewResolver> viewResolvers() {
    return viewResolvers;
  }

  @NotNull
  public static ViewResolverSettingBuilder builder() {
    return new ViewResolverSettingBuilder();
  }

  final public static class ViewResolverSettingBuilder {
    private ViewResolverSettingBuilder() {
    }

    private ApplicationContext applicationContext;
    private final List<ViewResolver> viewResolvers = new LinkedList<>();

    @NotNull
    public ViewResolverSettingBuilder applicationContext(@Nullable ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
      return this;
    }

    @NotNull
    public ViewResolverSettingBuilder registerViewResolvers(@Nullable ViewResolver... viewResolvers) {
      if (ArrayUtils.isEmpty(viewResolvers)) {
        return this;
      }
      assert viewResolvers != null;
      for (ViewResolver viewResolver : viewResolvers) {
        if (viewResolver == null) {
          continue;
        }
        if (this.viewResolvers.stream().noneMatch(e -> e == viewResolver)) {
          this.viewResolvers.add(viewResolver);
        }
      }
      return this;
    }

    @NotNull
    public ViewResolverSetting build() {
      return new ViewResolverSetting(this);
    }
  }

}
