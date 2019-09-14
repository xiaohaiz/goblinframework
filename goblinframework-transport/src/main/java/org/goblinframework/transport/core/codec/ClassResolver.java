package org.goblinframework.transport.core.codec;

import io.netty.handler.codec.serialization.ClassResolvers;
import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.core.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ClassResolver implements org.goblinframework.core.transcoder.ClassResolver {

  public static final ClassResolver INSTANCE = new ClassResolver();

  private final io.netty.handler.codec.serialization.ClassResolver delegator;

  private ClassResolver() {
    ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    this.delegator = ClassResolvers.softCachingConcurrentResolver(classLoader);
  }

  public io.netty.handler.codec.serialization.ClassResolver getDelegator() {
    return delegator;
  }

  @NotNull
  @Override
  public Class<?> resolve(@NotNull String className) throws ClassNotFoundException {
    return delegator.resolve(className);
  }

  @Install
  public static class Installer implements org.goblinframework.core.transcoder.ClassResolver {
    @NotNull
    @Override
    public Class<?> resolve(@NotNull String className) throws ClassNotFoundException {
      return INSTANCE.resolve(className);
    }
  }
}
