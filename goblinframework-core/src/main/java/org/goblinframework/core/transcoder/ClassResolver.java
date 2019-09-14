package org.goblinframework.core.transcoder;

public interface ClassResolver {

  Class<?> resolve(String className) throws ClassNotFoundException;

}
