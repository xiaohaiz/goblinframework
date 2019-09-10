package org.goblinframework.webmvc.setting;

import org.goblinframework.core.util.ArrayUtils;
import org.goblinframework.webmvc.interceptor.Interceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

final public class InterceptorSetting {

  private final boolean includeDefaultInterceptors;
  private final ApplicationContext applicationContext;
  private final List<Interceptor> interceptors;

  private InterceptorSetting(@NotNull InterceptorSettingBuilder builder) {
    this.includeDefaultInterceptors = builder.includeDefaultInterceptors;
    this.applicationContext = builder.applicationContext;
    this.interceptors = Collections.unmodifiableList(builder.interceptors);
  }

  public boolean includeDefaultInterceptors() {
    return includeDefaultInterceptors;
  }

  @Nullable
  public ApplicationContext applicationContext() {
    return applicationContext;
  }

  @NotNull
  public List<Interceptor> interceptors() {
    return interceptors;
  }

  @NotNull
  public static InterceptorSettingBuilder builder() {
    return new InterceptorSettingBuilder();
  }

  final public static class InterceptorSettingBuilder {

    private boolean includeDefaultInterceptors = true;
    private ApplicationContext applicationContext;
    private final List<Interceptor> interceptors = new LinkedList<>();

    private InterceptorSettingBuilder() {
    }

    @NotNull
    public InterceptorSettingBuilder includeDefaultInterceptors(boolean includeDefaultInterceptors) {
      this.includeDefaultInterceptors = includeDefaultInterceptors;
      return this;
    }

    @NotNull
    public InterceptorSettingBuilder applicationContext(@Nullable ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
      return this;
    }

    @NotNull
    public InterceptorSettingBuilder registerInterceptors(@Nullable Interceptor... interceptors) {
      if (ArrayUtils.isEmpty(interceptors)) {
        return this;
      }
      assert interceptors != null;
      for (Interceptor interceptor : interceptors) {
        if (interceptor == null) {
          continue;
        }
        if (this.interceptors.stream().noneMatch(e -> e == interceptor)) {
          this.interceptors.add(interceptor);
        }
      }
      return this;
    }

    @NotNull
    public InterceptorSetting build() {
      return new InterceptorSetting(this);
    }
  }
}
