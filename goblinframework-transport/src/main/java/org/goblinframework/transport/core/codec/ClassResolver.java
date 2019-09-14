package org.goblinframework.transport.core.codec;

import io.netty.handler.codec.serialization.ClassResolvers;
import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.core.util.ClassUtils;

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

  @Override
  public Class<?> resolve(String className) throws ClassNotFoundException {
    return delegator.resolve(className);
  }

  @Install
  public static class Installer implements org.goblinframework.core.transcoder.ClassResolver {
    @Override
    public Class<?> resolve(String className) throws ClassNotFoundException {
      return INSTANCE.resolve(className);
    }
  }
}
